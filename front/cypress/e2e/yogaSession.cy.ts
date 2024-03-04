/// <reference types="Cypress" />

describe('Session creation form', () => {
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

    cy.intercept('GET', '/api/teacher', {
      body: [
        { id: 1, firstName: 'Margot', lastName: 'DELAHAYE' },
        { id: 2, firstName: 'Hélène', lastName: 'THIERCELIN' },
      ],
    });

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

    cy.wait('@loginSuccess');
    cy.url().should('include', '/sessions');
    cy.get('span.ml1').contains('Create').should('exist').click();
  });

  it('should display session details correctly', () => {
    cy.get('h1').contains('Create session').should('exist');
    cy.get('form').should('exist');
    cy.get('input[formControlName=name]')
      .should('exist')
      .type('Session de Yoga Yin avec Margot');
    cy.get('input[formControlName=date]').should('exist').type('2023-03-01');
    cy.get('mat-label').contains('Teacher').should('exist');
    cy.get('mat-select[formControlName="teacher_id"]').should('be.visible');

    cy.get('mat-select[formControlName="teacher_id"]').click();

    cy.get('mat-option').contains('Margot DELAHAYE').click();

    cy.get('textarea[formControlName="description"]')
      .should('exist')
      .type(
        'Un cours de yoga Yin se concentre sur les étirements en profondeur en maintenant les postures de yoga pendant de longues périodes ( 3 à 8 minutes).'
      );
    cy.intercept('POST', '/api/session').as('goToSessions');
    cy.get('button[type="submit"]').should('exist').click({ force: true });
    cy.wait('@goToSessions');
    cy.url().should('include', '/sessions');
  });

  it('should disable de save button if name missing', () => {
    cy.get('h1').contains('Create session').should('exist');
    cy.get('form').should('exist');
    cy.get('input[formControlName=name]')
      .should('exist')
      .type('Session de Yoga Yin avec Margot');
    cy.get('input[formControlName=date]').should('exist');
    cy.get('mat-label').contains('Teacher').should('exist');
    cy.get('mat-select[formControlName="teacher_id"]').should('be.visible');

    cy.get('mat-select[formControlName="teacher_id"]').click();

    cy.get('mat-option').contains('Margot DELAHAYE').click();

    cy.get('textarea[formControlName="description"]')
      .should('exist')
      .type(
        'Un cours de yoga Yin se concentre sur les étirements en profondeur en maintenant les postures de yoga pendant de longues périodes ( 3 à 8 minutes).'
      );
    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('should disable de save button if date missing', () => {
    cy.get('h1').contains('Create session').should('exist');
    cy.get('form').should('exist');
    cy.get('input[formControlName=name]').should('exist');

    cy.get('input[formControlName=date]').should('exist').type('2023-03-01');
    cy.get('mat-label').contains('Teacher').should('exist');
    cy.get('mat-select[formControlName="teacher_id"]').should('be.visible');

    cy.get('mat-select[formControlName="teacher_id"]').click();

    cy.get('mat-option').contains('Margot DELAHAYE').click();

    cy.get('textarea[formControlName="description"]')
      .should('exist')
      .type(
        'Un cours de yoga Yin se concentre sur les étirements en profondeur en maintenant les postures de yoga pendant de longues périodes ( 3 à 8 minutes).'
      );
    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('should disable de save button if description missing', () => {
    cy.get('h1').contains('Create session').should('exist');
    cy.get('form').should('exist');
    cy.get('input[formControlName=name]').should('exist');

    cy.get('input[formControlName=date]').should('exist').type('2023-03-01');
    cy.get('mat-label').contains('Teacher').should('exist');
    cy.get('mat-select[formControlName="teacher_id"]').should('be.visible');

    cy.get('mat-select[formControlName="teacher_id"]').click();

    cy.get('mat-option').contains('Margot DELAHAYE').click();

    cy.get('textarea[formControlName="description"]').should('exist');

    cy.get('button[type="submit"]').should('be.disabled');
  });


});
