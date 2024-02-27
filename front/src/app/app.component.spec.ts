import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatToolbarModule } from '@angular/material/toolbar';
import { Router } from '@angular/router';

import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';


describe('AppComponent', () => {
  let router: Router;
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        [RouterTestingModule.withRoutes([])],
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();                           // Compile les composants pour les préparer au test

    fixture = TestBed.createComponent(AppComponent);  // Crée une instance du composant à tester
    component = fixture.componentInstance;            // Obtient une instance du composant à partir du fixture
    fixture.detectChanges();                          // On effectue la détection des changements pour initialiser le composant
    router = TestBed.get(Router);                     // On obtient une instance du routeur
  });
  

    // On teste si le composant est bien créé
  it('should create the app', () => {
    expect(component).toBeTruthy();                       // On vérifie que l'instance du composant est 
  });


  it('should navigate to "" on logout', () => {
    const navigateSpy = jest.spyOn(router, 'navigate');   // On espionne sur la méthode navigate du router
    component.logout();                                   // On éxécute la méthode logout
    expect(navigateSpy).toHaveBeenCalledWith(['']);       // On vérifie si la navigation a été appelée avec le bon argument
  });
  
});
