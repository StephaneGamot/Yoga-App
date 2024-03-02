/// <reference types="Cypress" />

describe('user information test', () => {
  beforeEach(() => {
    cy.visit('/login');                         // On commence le test à la page /login
    cy.intercept('POST', '/api/auth/login', {   // On intercepte une requete POST 
     
      statusCode: 200,                          // Et on simule une réponse 
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    }).as('loginSuccess');                      // On lui donne un nom pour s'en servir plus tard

    cy.intercept('GET', '/api/user/1', {        // On intercepte une requete GET

      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'Stéphane',
        lastName: 'Gmt',
        email: 'yoga@studio.com',
        admin: true,
        createdAt: '2024-03-01',
        updatedAt: '2024-03-01',
      },
    }).as('getUserInfo');                       

    cy.get('input[formControlName=email]').type('yoga@studio.com');            // Remplissage et soumission du formulaire avec l'Email
    cy.get('input[formControlName=password]').type('test!1234{enter}{enter}'); // Remplissage et soumission du formulaire avec le mot de passe

    cy.wait('@loginSuccess');                                                  // on attend que loginSuccess (plus haut) se remplisse
    cy.url().should('include', '/sessions');                                   // On vérifie qu'il nous envoie bien vers /sessions
  });


  // on va sur la page /me, on peut y récuperer nos informations
  it('should go to to me and get information about me', () => {
    cy.contains('span.link', 'Account').click(); // On clique sur le lien "Account",
    cy.wait('@getUserInfo');                     // On attend que userinfo ( plus haut) se remplisse
    cy.url().should('include', '/me');           // On vérifie qu'il nous envoie bien vers /me

    cy.contains('Name: ').should('be.visible');  // On vérifie que l'élément Name est bien visible
    cy.contains('Email:').should('be.visible');  // On vérifie que l'élément Email est bien visible
  });


  // On doit pouvoir revenir en arriere 
  it('should go back to sessions', () => {
    cy.contains('span.link', 'Account').click(); // On clique sur le lien "Account",
    cy.wait('@getUserInfo');                     // On attend que userinfo ( plus haut) se remplisse
    cy.url().should('include', '/me');           // On vérifie qu'il nous envoie bien vers /me

    cy.get('button.mat-icon-button').click();   // On clique sur le lien "Back",
    cy.url().should('include', '/sessions');    // On vérifie qu'il nous envoie bien vers /sessions
  });
});
