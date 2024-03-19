/// <reference types="Cypress" />

describe('Login spec', () => {
  // test de connection réussi
  it('Login successfull', () => {
    cy.visit('/login');                                              // On commence directement à la page /login
    cy.intercept('POST', '/api/auth/login', {                        // On intercepte la requête POST vers l'API Login et on simule une réponse de succès 200
      body: {                                                        // On simule une réponse de succès avec des données utilisateur
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    cy.intercept('GET', '/api/session', []).as('session');           // On intercepte les requêtes GET à /api/session et on simule une réponse avec un tableau vide.

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]') 
      .type(`${'test!1234'}                                                                                                          
    {enter}{enter}`);
    cy.url().should('include', '/sessions');                         // On s'assure que l'URL inclut '/sessions' et que l'utilisateur a bien envoyé vers /sessions
  });

  //On doit retourner une erreur si l'Email n'est pas valide
  it('should return error if email is not valid', () => {
    cy.visit('/login');                                              // On commence directement à la page /login

    cy.get('input[formControlName=email]').type('aaa');              // On remplit le champ de saisie 
    cy.get('input[formControlName=password]').type(
      `${'tesdfegergergergergergt'}{enter}{enter}`
    );

    cy.get('.error').should('be.visible');                           // On s'assure que le message d'erreur est bien visible
  });

  // Si l'email est vide 
  it('should return error if email is empty', () => {
    cy.visit('/login');                                              // On commence directement à la page /login

    cy.get('input[formControlName=email]').type(' ');
    cy.get('input[formControlName=password]').type(
      `${'testertergergz'}{enter}{enter}`
    );

    cy.get('.error').should('be.visible');                           // On s'assure que le message d'erreur est bien visible
  });

  // Mot de passe vide
  it('should return error if password is empty', () => {
    cy.visit('/login');

    cy.get('input[formControlName=email]').type('stephane@gmail.com');
    cy.get('input[formControlName=password]').type(`${' '}{enter}{enter}`);

    cy.get('.error').should('be.visible');              // On s'assure que le message d'erreur est bien visible
  });
});
