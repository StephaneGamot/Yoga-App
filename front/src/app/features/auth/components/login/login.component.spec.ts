import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { AuthService } from '../../services/auth.service';
import { throwError, of } from 'rxjs';
import { Router } from '@angular/router';
import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [AuthService],
      imports: [
        RouterTestingModule.withRoutes([]), 
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });


// Ce test vérifie si le composant LoginComponent est créé correctement, il n'est pas nul ou indéfini (Test Unitaire)
  it('should create', () => {
    expect(component).toBeTruthy();  
  });


  // Ce test vérifie que le formulaire est invalide lorsque les champs sont vides. (Test Unitaire)
  it('form should be invalid when empty', () => {
    expect(component.form.valid).toBeFalsy();       // On vérifie que la propriété valid du formulaire est false, lorsque le champ est vide.
  });


  // On vérifie la validité du champ email (Test Unitaire)
  it('email field validity', () => {
    let email = component.form.controls['email'];   // Creation d'une const qui relie au champ email dans le formulaire.
    email.setValue('');                             // On définit la valeur du champ email à une chaîne vide pour tester
    expect(email.valid).toBeFalsy();                // Si il est vide, il est non validé.
    expect(email.errors?.['required']).toBeTruthy();// Vérifie que 'required' est présent , car obligatoire mais vide.
  
    email.setValue('test');                         // On éfinit la valeur du champ email à 'test', qui n'est pas valide !.
    expect(email.valid).toBeFalsy();                // On attend a ce qu'il le retourne NON valide
    expect(email.errors?.['email']).toBeTruthy();   // On vérifie que l'erreur 'email' est présente, indiquant que le format est NON valide.
  });
  

  // On vérifie la validité du mot de passe (Test Unitaire)
  it('password field validity', () => {
    let password = component.form.controls['password']; // Creation d'une const qui relie au champ password dans le formulaire.
    password.setValue('');                              // On définit sa valeur à une chaîne vide pour tester
    expect(password.valid).toBeFalsy();                 // Si il est vide, il est non validé.
    expect(password.errors?.['required']).toBeTruthy(); // On vérifie que l'erreur 'required' est présente
  });
  
  // On verifie la connection lors du click de soumission (Test d'Intégration)
  it('should navigate to "/sessions" on successful login', async () => {
    const navigateSpy = jest.spyOn(router, 'navigate');     // On va espionner la méthode navigate du router    
    jest.spyOn(authService, 'login').mockReturnValue(of({   // On va simuler une réponse réussie de la méthode login de authService
      id: 1, username: 'testUser', token: 'testToken', type: 'type', firstName: 'Test', lastName: 'User', admin: true
    }));
    component.submit();                                     // On appele la méthode submit du composant
    await fixture.whenStable();                             // On attend les opérations asynchrones
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);// On vérifie que la navigation a été appelée avec le bon chemin
  });
  
// On verifie la gestion des erreurs lors d'une mauvaise connexion (Test d'Intégration)
it('should handle error for incorrect login/password', () => {
  const loginSpy = jest.spyOn(authService, 'login').mockReturnValue(throwError(() => new Error('Incorrect login/password'))); // On crée un "espion" qui va simuler une réponse d'erreur quand la méthode sera appelée
  component.form.controls['email'].setValue('user@example.com');
  component.form.controls['password'].setValue('wrongpassword');
  component.submit();
  expect(component.onError).toBe(true);                           // On vérifie que le composant a bien détecté une erreur à la suite de la tentative de connexion
  expect(loginSpy).toHaveBeenCalled();                            // On s'assure que la méthode 'login' a été appelée pendant le processus (grâce à l'espion).
});

 // Vérification de l'affichage d'erreur en l'absence d'un champ obligatoire (Test d'Intégration)
 it('should display an error message if required fields are missing', () => {
  component.form.controls['email'].setValue('');                               // On laisse volontairement le champ email vide
  component.submit();                                                          // On simule la soumission du formulaire.
  expect(component.form.controls['email'].valid).toBeFalsy();                  // On vérifie que le champ email est considéré comme invalide puisqu'il est vide et il est sensé être validé
  expect(component.form.controls['email'].errors?.['required']).toBeTruthy();  // On vérifie qu'une erreur a bien été levé
 
  component.form.controls['password'].setValue('');
  component.submit();  
  expect(component.form.controls['password'].valid).toBeFalsy();
  expect(component.form.controls['password'].errors?.['required']).toBeTruthy();
});

  // On vérifie qu'on déclenche une Erreur si la fonction login échoue (Test d'Intégration)
  it('should set onError to true if login fails', () => {
    const authService = TestBed.inject(AuthService);  // En créant une const cela permet au test d'accéder à authService.
    jest.spyOn(authService, 'login').mockReturnValue(throwError(() => new Error('error'))); // Création d'un espion qui surveille login, ici on simule une erreur quand il est appelé 
    component.submit();                                  // On appelle la méthode submit du formulaire de login        
    expect(component.onError).toBe(true);                // On verifie qu'en cas d'échec le Erreur est bien déclenché
  });
  
// On vérifie que la methode est bien appelé quand on clique sur submit (Test d'Intégration)
  it('should track the login method call', () => {
    const authService = (component as any).authService;  // En créant une const cela permet au test d'accéder à authService.
    const loginSpy = jest.spyOn(authService, 'login').mockReturnValue(of({})); // Création d'un espion qui surveille login, qui nous renvoie un objet vide (simulation)
    component.submit();                                  // On appelle la méthode submit du formulaire de login
    expect(loginSpy).toHaveBeenCalled();                 // L'espion nous informe qu'il a été appelé quand on a soumis le formulaire ... AuthService
  });
});
