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
   
    cy.intercept('POST', '/api/auth/logout', {   // On intercepte la requête de déconnexion
      statusCode: 200, // On simule une réponse de succès pour la déconnexion
    }).as('logout');

    cy.contains('.link', 'Logout').click(); 

    cy.url().should('include', '/');
  });


  it('should display session details correctly', () => {
    cy.get('mat-card-title h1').should('not.be.empty');
    
    // Vérifie que le nombre de participants et la date de la session sont affichés
    cy.get('span.ml1').contains('attendees').should('not.be.empty');
    cy.get('span.ml1').contains('Create at:').should('not.be.empty');
    cy.get('span.ml1').contains('Last update:').should('not.be.empty');
  });

 
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
  

})