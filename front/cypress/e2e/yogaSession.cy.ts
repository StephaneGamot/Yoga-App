/// <reference types="Cypress" />

describe('Session creation and update form', () => {
  beforeEach(() => {
    cy.visit('/login');
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    }).as('loginSuccess');

    cy.get('input[formControlName=email]').type('yoga@studio.com'); 
    cy.get('input[formControlName=password]').type('test!1234{enter}{enter}'); 

    cy.wait('@loginSuccess');
    cy.url().should('include', '/sessions');
    cy.get('span.ml1').contains('Create').should('exist').click();
  });

  it('should display session details correctly', () => {
    cy.get('h1').contains('Create session').should('exist');
    cy.get('form').should('exist');
    cy.get('input[FormControlName=name]').should('exist').type('Session de Yoga Yin avec Tiffa');
    cy.get('input[formControlName=date]').should('exist').type('2023-03-01');
    cy.get('mat-label').contains('Teacher').should('exist');
   


    
    cy.get('mat-label').contains('Description').should('exist');
  });
});
