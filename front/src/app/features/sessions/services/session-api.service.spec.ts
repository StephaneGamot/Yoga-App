import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;                     

 beforeEach(() => {
    TestBed.configureTestingModule({        // permet de mocker les requêtes HTTP.
      imports: [HttpClientTestingModule],   // On l'utilisera pour simuler les réponses aux requêtes HTTP faites par le service.
      providers: [SessionApiService],
    });
    service = TestBed.inject(SessionApiService);            // On initialise le service avec l'injecteur de test
    httpMock = TestBed.inject(HttpTestingController);       // On initialise le mock du contrôleur HTTP pour simuler les réponses HTTP
  });

  afterEach(() => {
    httpMock.verify();                                      // On s'assure qu'il n'y a pas de requêtes en attente après chaque test.
  });


  // On vérifie que le service a bien été créé
  it('should be created', () => {
    expect(service).toBeTruthy();                           // On verifie qu'il répond    
  });


// On teste la récupération de toutes les sessions
  it('should retrieve all sessions', () => {
    service.all().subscribe(sessions => {                   // On appelle la méthode all() du service et on souscrit au résultat
      expect(sessions).toBeTruthy();                        // On vérifie simplement que la réponse est "truthy"
    });

    const req = httpMock.expectOne(service['pathService']);
    expect(req.request.method).toBe('GET');                 // On simule une réponse HTTP vide pour le test
    req.flush([]);                                          // On simule une réponse vide pour ce test
  });


  // On vérifie que le détail d'une session peut être récupéré par un ID
  it('should retrieve session detail by id', () => {
    const testId = '123';                                   // Création d'un ID fictif pour le test
    service.detail(testId).subscribe(session => {           // On appelle la méthode detail() du service avec cet ID et souscrit au résultat
      expect(session).toBeTruthy();                         // Vérifie que la réponse est "truthy"
    });
  
    const req = httpMock.expectOne(`${service['pathService']}/${testId}`);   // On attend une requête HTTP à l'URL pour cet ID 
    expect(req.request.method).toBe('GET');                                  // On vérifie que la méthode est bien GET
    req.flush({});                                                           // On simule une réponse vide pour ce test
  });

  
});
