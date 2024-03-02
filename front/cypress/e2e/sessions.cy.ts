/// <reference types="Cypress" />

describe('Sessions test', () => {

    beforeEach(() => {
        cy.visit('/login');
        cy.intercept('POST', '/api/auth/login', {

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
      
    })
    // Test de déconnexion réussie
it('Logout successfull', () => {
   // cy.visit('/login'); // On part du principe que l'utilisateur est bien connecté et visite la page de session

   
    cy.intercept('POST', '/api/auth/logout', {   // On intercepte la requête de déconnexion
      statusCode: 200, // On simule une réponse de succès pour la déconnexion
    }).as('logout');

    cy.get('.link').contains('Logout').click();

    cy.url().should('include', '/');
  });

})