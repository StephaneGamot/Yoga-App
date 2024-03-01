describe('Login spec', () => {
  it('Login successfull', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []
    ).as('session');

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    cy.url().should('include', '/sessions');
  });

  // Erreur pour l'Email
  it('should return error if email is not valid', () => {
    cy.visit('/login');

    cy.get('input[formControlName=email]').type('aaa');
    cy.get('input[formControlName=password]').type(`${'tesdfegergergergergergt'}{enter}{enter}`);

    cy.get('.error').should('be.visible');
  });

  // Email vide 
  it('should return error if email is empty', () => {
    cy.visit('/login');

    cy.get('input[formControlName=email]').type(' ');
    cy.get('input[formControlName=password]').type(`${'testertergergz'}{enter}{enter}`);

    cy.get('.error').should('be.visible');
  });

  // Mot de passe vide 
  it('should return error if password is empty', () => {
    cy.visit('/login');

    cy.get('input[formControlName=email]').type('stephane@gmail.com');
    cy.get('input[formControlName=password]').type(`${' '}{enter}{enter}`);

    cy.get('.error').should('be.visible');
  });







});
