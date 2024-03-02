/// <reference types="Cypress" />

describe('Testing the Register Component', () => {
  beforeEach(() => {
    cy.visit('/register');                                            // Avant chaque test, on navigue à la page /register
  });

  it('Register successfull', () => {
    cy.intercept('POST', '/api/auth/register', {});                   // On intercepte une requete POST et on simule une réponse vide

    cy.get('input[formControlName=firstName]').type('Stephane');      // On remplit le champ prénom avec "Stephane".
    cy.get('input[formControlName=lastName]').type('Gmt');            // On remplit le champ nom avec "Gmt".
    cy.get('input[formControlName=email]').type('stephane@gmail.com');// On remplit le champ email avec "stephane@gmail.com".
    cy.get('input[formControlName=password]').type(                   // On remplit le champ mot de passe avec "secretPassword"
      `${'secretPassword'}{enter}{enter}`                             // Et on soumet le formulaire.
    );

    cy.url().should('include', '/login');                             // On vérifie que l'URL inclut bien '/login' après l'inscription, indiquant une =>
  });


  // Si l'utilisateur est deja pris (email)
  it('Register already been taken', () => {
    cy.intercept('POST', '/api/auth/register', { // On intercepte les requêtes POST vers l'endpoint /regiter et on simule une réponse d'erreur avec le statut 400.
      statusCode: 400,
      body: {
        message: 'Email already taken',
      },
    });

    cy.get('input[formControlName=firstName]').type('Stephane');
    cy.get('input[formControlName=lastName]').type('Gmt');
    cy.get('input[formControlName=email]').type('stephane@gmail.com');
    cy.get('input[formControlName=password]').type(
      `${'secretPassword'}{enter}`
    );

    cy.get('.error').should('be.visible');       // On vérifie que le message d'erreur est bien visible sur la page.
  });


  // on teste si le boutton submit refuse en cas de l'oublie du prénom 
  it('should disable submit button if firstName is empty', () => {
    cy.get('input[formControlName=lastName]').type('Gmt');
    cy.get('input[formControlName=email]').type('stephane@gmail.com');
    cy.get('input[formControlName=password]').type('secretPassword');
    cy.get('input[formControlName=firstName]').clear();

    cy.get('button[type=submit]').should('be.disabled');  // On s'assure que le bouton de soumission est bien désactivé.
  });


   // on teste si le boutton submit refuse en cas de l'oublie du nom 
  it('should disable submit button if lastName is empty', () => {
    cy.get('input[formControlName=firstName]').type('Stephane');
    cy.get('input[formControlName=email]').type('stephane@gmail.com');
    cy.get('input[formControlName=password]').type('secretPassword');
    cy.get('input[formControlName=lastName]').clear();

    cy.get('button[type=submit]').should('be.disabled');  // On s'assure que le bouton de soumission est bien désactivé.
  });


   // on teste si le boutton submit refuse en cas de l'oublie de l'email 
  it('should disable submit button if email is empty', () => {
    cy.get('input[formControlName=firstName]').type('Stephane');
    cy.get('input[formControlName=lastName]').type('Gmt');
    cy.get('input[formControlName=password]').type('secretPassword');
    cy.get('input[formControlName=email]').clear();

    cy.get('button[type=submit]').should('be.disabled');  // On s'assure que le bouton de soumission est bien désactivé.
  });


   // on teste si le boutton submit refuse en cas de l'oublie du mot de passe
  it('should disable submit button if password is empty', () => {
    cy.get('input[formControlName=firstName]').type('Stéphane');
    cy.get('input[formControlName=lastName]').type('Gmt');
    cy.get('input[formControlName=email]').type('stephane@gmail.com');
    cy.get('input[formControlName=password]').clear();

    cy.get('button[type=submit]').should('be.disabled');  // On s'assure que le bouton de soumission est bien désactivé.
  });
});
