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
import { SessionService } from 'src/app/services/session.service';
import { AuthService } from '../../services/auth.service';
import { throwError, of } from 'rxjs';


import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService],
      imports: [
        RouterTestingModule,
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
  

  // On vérifie qu'on déclenche une Erreur si la fonction login échoue (Test d'Intégration)
  it('should set onError to true if login fails', () => {
    const authService = (component as any).authService;  // En créant une const cela permet au test d'accéder à authService.
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
