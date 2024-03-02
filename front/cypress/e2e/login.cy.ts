/* eslint-disable */

describe('Login spec', () => {
  // test de connection réussi
  
  it('Login successfull', () => {
    cy.visit('/login'); // Visite la page de connexion
    // @ts-ignore
    cy.intercept('POST', '/api/auth/login', {
      // On intercepte la requête POST vers l'API Login et on simule une réponse de succès 200
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    cy.intercept('GET', '/api/session', []).as('session'); // On intercepte la requête GET vers '/api/session' et simule une réponse vide

    cy.get('input[formControlName=email]').type('yoga@studio.com'); // On remplit l'input de email
    cy.get('input[formControlName=password]') //
      .type(`${'test!1234'}                                                                                                          
    {enter}{enter}`); // Apres avoir remplit l'input du password on envoie les données

    cy.url().should('include', '/sessions'); // On vérifie si l'URL inclut '/sessions', ce qui indique une redirection après la connexion réussie
  });

  // Erreur pour l'Email
  it('should return error if email is not valid', () => {
    cy.visit('/login');

    cy.get('input[formControlName=email]').type('aaa');
    cy.get('input[formControlName=password]').type(
      `${'tesdfegergergergergergt'}{enter}{enter}`
    );

    cy.get('.error').should('be.visible');
  });

  // Email vide
  it('should return error if email is empty', () => {
    cy.visit('/login');

    cy.get('input[formControlName=email]').type(' ');
    cy.get('input[formControlName=password]').type(
      `${'testertergergz'}{enter}{enter}`
    );

    cy.get('.error').should('be.visible');
  });

  // Mot de passe vide
  it('should return error if password is empty', () => {
    cy.visit('/login');

    cy.get('input[formControlName=email]').type('stephane@gmail.com');
    cy.get('input[formControlName=password]').type(`${' '}{enter}{enter}`);

    cy.get('.error').should('be.visible');
  });

  // Test de déconnexion réussie
  it('Logout successfull', () => {
    cy.visit('/login'); // On part du principe que l'utilisateur est bien connecté et visite la page de session

    cy.intercept('POST', '/api/auth/login', {
      // On intercepter la requête POST envoyée lors de la connexion et on simuler une réponse réussie
      statusCode: 200, // On s'ssure que la réponse simulée indique 200
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    }).as('loginSuccess'); // On lui donne un nom pour pouvoir s'en reservir

    cy.get('input[formControlName=email]').type('yoga@studio.com'); // On remplit le formulaire de connexion email
    cy.get('input[formControlName=password]').type('test!1234{enter}{enter}'); //   On remplit le mot de passe et on le soumet

    cy.wait('@loginSuccess');
    cy.url().should('include', '/sessions');

    cy.intercept('POST', '/api/auth/logout', {   // On intercepte la requête de déconnexion
      statusCode: 200, // On simule une réponse de succès pour la déconnexion
    }).as('logout');

    cy.get('.link').contains('Logout').click();

    cy.url().should('include', '/');
  });
});
