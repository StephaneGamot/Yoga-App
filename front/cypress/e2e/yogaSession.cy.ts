/// <reference types="Cypress" />

describe('Testing a yoga session with user ', () => {
  const yogaSessionResponse = {
    body: {
      id: 1,
      name: 'session de yoga Yin',
      description: 'On prend le temps de respirer',
      teacher_id: 2,
      users: [],
    },
  };

  beforeEach(() => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'username',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false,
      },
    });

    cy.intercept('GET', '/api/teacher/2', {
      body: [{ id: 2, firstName: 'Hélène', lastName: 'THIERCELIN' }],
    });

    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: 'session de yoga',
          description: 'On fait du bien à son corps',
          teacher_id: 2,
          users: [],
        },
      ],
    });

    cy.intercept('GET', '/api/session/1', yogaSessionResponse);

    cy.get('input[formControlName=email]').type('stephane@gmail.com');
    cy.get('input[formControlName=password]').type(
      `${'password'}{enter}{enter}`
    );
  });
  it('should participate at a yoga session', () => {
    cy.intercept('POST', '/api/session/1/participate/1', {});
    cy.url().should('include', '/sessions');
    cy.get('.mat-card-actions > :nth-child(1)').click();
    cy.url().should('include', '/sessions/detail');

    cy.get('button:contains("Participate")').should('exist');

    cy.get('button:contains("Do not participate")').should('not.exist');

    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'session de yoga Yin',
        description: 'On prend le temps de respirer',
        teacher_id: 2,
        users: [1],
      },
    });

    cy.get('button:contains("Participate")').click();

    cy.get('button:contains("Do not participate")').should('exist');
  });

  it('should not participate at a yoga session', () => {
    cy.intercept('POST', '/api/session/1/participate/1', {});
    cy.url().should('include', '/sessions');
    cy.get('.mat-card-actions > :nth-child(1)').click();
    cy.url().should('include', '/sessions/detail');
    cy.get('button:contains("Participate")').should('exist');
    cy.get('button:contains("Do not participate")').should('not.exist');

    cy.intercept('GET', '/api/session/1', {
      ...yogaSessionResponse,
      body: { ...yogaSessionResponse.body, users: [1] },
    });

    cy.get('button:contains("Participate")').click();

    cy.get('button:contains("Do not participate")').should('exist');

    cy.intercept('DELETE', '/api/session/1/participate/1', {});

    cy.intercept('GET', '/api/session/1', yogaSessionResponse);

    cy.get('button:contains("Do not participate")').click();

    cy.get('button:contains("Participate")').should('exist');
  });
});
