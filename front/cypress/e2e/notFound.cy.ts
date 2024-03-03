/// <reference types="Cypress" />

// on teste la page 404
    describe('Testing page 404', () => {
        it('should display a 404 page', () => {
        
          cy.visit('/404', {failOnStatusCode: false});                 // Le deuxième argument empêche Cypress de rater le test si le statut HTTP est 404
          cy.contains('div', 'Page not found !').should('be.visible'); // Vérifie que le texte "Page not found !" est bien affiché sur la page
    
        });
      });