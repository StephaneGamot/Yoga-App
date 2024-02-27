import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';
import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';
import { By } from '@angular/platform-browser';


import { RegisterComponent } from './register.component';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  // Ce test vérifie que le formulaire est invalide lorsque les champs sont vides. (Test Unitaire)
  it('form should be invalid when empty', () => {
    expect(component.form.valid).toBeFalsy();       // On vérifie que la propriété valid du formulaire est false, lorsque le champ est vide.
  });


  // Il vérifie la validité du mail
  it('email field validity', () => {
    let email = component.form.controls['email'];    // On crée une variable qui pemert d'acceder au champ Email
    expect(email.valid).toBeFalsy();                 // si il est vide il doit être faux
    email.setValue('test');                          // On créé volontairement un email faux 
    expect(email.errors?.['email']).toBeTruthy();    // Il doit nous renvoyer une erreur 
    email.setValue('test@example.com');              // Création d'un email possible
    expect(email.valid).toBeTruthy();                // qui nous renvoie qu'il est correct
  });
  

  // On teste que les 4 champs existent
  it('should create form with four controls', () => {
    expect(component.form.contains('firstName')).toBeTruthy();  // Le champ firstName existe
    expect(component.form.contains('lastName')).toBeTruthy();   // Le champ lastName existe
    expect(component.form.contains('email')).toBeTruthy();      // Le champ email existe
    expect(component.form.contains('password')).toBeTruthy();   // Le champ password existe
});


// On teste qu'aucun champs ne soit vide 
it('should not submit form if it is invalid', () => {
    component.form.controls['firstName'].setValue('');  // Création d'une valeur vide ''
    component.form.controls['lastName'].setValue('');   // Création d'une valeur vide ''
    component.form.controls['email'].setValue('');      // Création d'une valeur vide ''
    component.form.controls['password'].setValue('');   // Création d'une valeur vide ''
    component.submit();                                 // On tente de soumettre
    expect(component.form.valid).toBeFalsy();           // Il doit retourner non valide
    
});


// On veut s'assurer que le composant tente bien de s'enregistrer via le service quand le formulaire est valide.
it('should call the register method on AuthService for a valid form submission', () => {
  const authServiceMock = TestBed.inject(AuthService);            // On injecte le AuthService dans le test pour pouvoir interagir avec
  const registerSpy = jest.spyOn(authServiceMock,'register').mockReturnValue(of(void 0)); // Création d'un espion, on simule une réponse de type void 
  component.form.controls['email'].setValue('test@example.com');  // On affecte une valeur valide à email
  component.form.controls['firstName'].setValue('John');          // On affecte une valeur valide à firstNAme
  component.form.controls['lastName'].setValue('Doe');            // On affecte une valeur valide à lastName
  component.form.controls['password'].setValue('Password123');    // On affecte une valeur valide à password
  component.submit();                                             // On tente de soumettre le formulaire
  expect(registerSpy).toHaveBeenCalled();                         // On vérifie que l'espion a été appelé, donc que AuthService a lui aussi été appellé
});


  // On vérifie qu'on déclenche une Erreur si la fonction register échoue (Test d'Intégration)
  it('should set onError to true if Register fails', () => {
    const authServiceMock = TestBed.inject(AuthService);         // On injecte le AuthService dans le test pour pouvoir interagir avec
    jest.spyOn(authServiceMock, 'register').mockReturnValue(throwError(() => new Error('error')));  // Création d'un espion qui surveille register, ici on simule une erreur quand il est appelé 
   component.submit();                                           // On tente de soumettre le formulaire
    expect(component.onError).toBe(true);                        // On verifie qu'en cas d'échec le Erreur est bien déclenché
  });
  

  // Ce test nous permet de savoir si le message d'error existe bien. NOT NULL
  it('should not display error message when onError is false', () => {
    component.onError = false;                             // On définit la propriété `onError` sur false pour simuler "aucune" erreur
    fixture.detectChanges();                               // On déclenche la détection de changements pour appliquer la mise à jour
    const compiled: HTMLElement = fixture.nativeElement;   // On récupère l'élément HTML pour accéder au DOM 
    const errorElement = compiled.querySelector('.error'); // On sélectionne l'élément avec la classe `.error` 
    expect(errorElement).toBeNull();                       // On vérifie que l'élément `.error` n'existe pas (est `null`)
  });


// On vérifie que le message d'erreur s'enclenche quand une erreur est détecté
  it('should display error message when onError is true', () => {
    component.onError = true;                                         // On définit la propriété `onError` sur true
    fixture.detectChanges();                                          // On déclenche la détection de changements pour appliquer la mise à jour
    const compiled: HTMLElement = fixture.nativeElement;              // On récupère l'élément HTML pour accéder au DOM 
    const errorElement = compiled.querySelector('.error');            // On sélectionne l'élément avec la classe `.error` 
    expect(errorElement!.textContent).toContain('An error occurred'); // On vérifie que le contenu textuel de l'élément contient le message d'erreur attendu.
  });
  
});
