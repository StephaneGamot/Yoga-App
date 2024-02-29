import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';
import { FormComponent } from './form.component';
import { Session } from '../../interfaces/session.interface';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';


describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSession: Session = {
    id: 1,
    name: 'New Yoga Class',
    date: new Date(),
    teacher_id: 1,
    description: 'A great yoga session',
    users: [],
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 123,
      token: 'Token',
      type: 'admin',
      username: 'adminUser',
      firstName: 'Admin',
      lastName: 'User',
    },
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
        NoopAnimationsModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService,
      ],
      declarations: [FormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

// (Test Unitaire)
  it('should create', () => {
    expect(component).toBeTruthy();                        // On vérifie que l'instance du composant a bien été créée
  });


  // (Test Unitaire)
  it('should initialize sessionForm for new session', () => {
    expect(component.sessionForm).toBeDefined();           // On vérifie que l'objet sessionForm est défini
    expect(component.onUpdate).toBeFalsy();                // On vérifie que le composant n'est pas en mode mise à jour
    expect(component.sessionForm?.value).toEqual({         // On vérifie que les valeurs initiales du formulaire sont vides, indiquant une nouvelle session
      name: '',
      date: '',
      teacher_id: '',
      description: '',
    });
  });


  //  On initialise le formulaire pour la création d'une session avec des champs vides (Test Unitaire)
  it('should initialize form for session creation with empty fields', () => {
    component.onUpdate = false;                                      // On définit le composant pour ne pas être en mode mise à jour
    fixture.detectChanges();                                         // On applique les changements détectés pour s'assurer que le formulaire est réinitialisé
    expect(component.sessionForm).toBeDefined();                     // On vérifie que le formulaire est bien défini
    expect(component.sessionForm?.get('name')?.value).toEqual('');   // On vérifie que chaque champ du formulaire est initialisé à vide
    expect(component.sessionForm?.get('date')?.value).toEqual('');
    expect(component.sessionForm?.get('teacher_id')?.value).toEqual('');
    expect(component.sessionForm?.get('description')?.value).toEqual(''); 
    expect(component.sessionForm?.invalid).toBeTruthy();             // On vérifie que le formulaire est considéré comme invalide (puisque tous les champs sont vides)
  });


  // On appele la méthode create sur sessionApiService lors de la soumission du formulaire pour la création (test d'intégration )
  it('should call create method on sessionApiService when form is submitted for creation', () => {
    component.onUpdate = false;                                 // On définit le composant pour ne pas être en mode mise à jour
    fixture.detectChanges();                                    // On applique les changements détectés pour préparer le formulaire à la soumission
    component.sessionForm?.setValue({                           // On initialise le formulaire avec des valeurs prédéfinies valides
      name: 'New Yoga Class',
      date: '2024-01-01',
      teacher_id: '1',
      description: 'A great yoga session',
    });

    const sessionApiService = TestBed.inject(SessionApiService);// On injecte SessionApiService
    const createSpy = jest                                      // Création d'un espion 
      .spyOn(sessionApiService, 'create')                       // Sur sa méthode 'create'
      .mockImplementation(() => of(mockSession));               // On simule la réponse de la méthode 'create'
    component.submit()                                          // On soumet le formulaire
    expect(createSpy).toHaveBeenCalledWith(expect.anything());  // Et on vérifie que la méthode 'create' a été appelée avec n'importe quel argument
  });
});
