describe('Testing the Register Component', () => {
    beforeEach(() => {
        cy.visit('/register');
    });
    
    it('Register successfull', () => {
      cy.intercept('POST', '/api/auth/register', { 
      });

      cy.get('input[formControlName=firstName]').type('Stephane');
      cy.get('input[formControlName=lastName]').type('Gmt');
      cy.get('input[formControlName=email]').type('stephane@gmail.com');
      cy.get('input[formControlName=password]').type(`${'secretPassword'}{enter}{enter}`);

      cy.url().should('include', '/login');
    
    })

    it('Register already been taken', () => {
        cy.intercept('POST', '/api/auth/register', {
            statusCode: 400,
            body: {
                message: 'Email already taken' 
            }
        });
        
        cy.get('input[formControlName=firstName]').type('Stephane');
        cy.get('input[formControlName=lastName]').type('Gmt');
        cy.get('input[formControlName=email]').type('stephane@gmail.com');
        cy.get('input[formControlName=password]').type(`${'secretPassword'}{enter}`);
       
        cy.get('.error').should('be.visible');
    });
    

  it('should disable submit button if firstName is empty', () => {
    
    cy.get('input[formControlName=lastName]').type('Gmt');
    cy.get('input[formControlName=email]').type('stephane@gmail.com');
    cy.get('input[formControlName=password]').type('secretPassword');
    cy.get('input[formControlName=firstName]').clear();
    
    cy.get('button[type=submit]').should('be.disabled');
  });

  it('should disable submit button if lastName is empty', () => {
  
    cy.get('input[formControlName=firstName]').type('Stephane');
    cy.get('input[formControlName=email]').type('stephane@gmail.com');
    cy.get('input[formControlName=password]').type('secretPassword');
    cy.get('input[formControlName=lastName]').clear();

    cy.get('button[type=submit]').should('be.disabled');
  });

  it('should disable submit button if email is empty', () => {

    cy.get('input[formControlName=firstName]').type('Stephane');
    cy.get('input[formControlName=lastName]').type('Gmt');
    cy.get('input[formControlName=password]').type('secretPassword');
    cy.get('input[formControlName=email]').clear();

    cy.get('button[type=submit]').should('be.disabled');
  });

  it('should disable submit button if password is empty', () => {

    cy.get('input[formControlName=firstName]').type('Stéphane');
    cy.get('input[formControlName=lastName]').type('Gmt');
    cy.get('input[formControlName=email]').type('stephane@gmail.com');
    cy.get('input[formControlName=password]').clear();

    cy.get('button[type=submit]').should('be.disabled');
  });
    
});