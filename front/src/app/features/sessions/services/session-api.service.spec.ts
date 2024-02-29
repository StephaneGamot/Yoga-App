import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { expect } from '@jest/globals';
import { Session } from '../interfaces/session.interface';
import { SessionApiService } from './session-api.service';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;                     

 beforeEach(() => {
    TestBed.configureTestingModule({                        // permet de mocker les requêtes HTTP.
      imports: [HttpClientTestingModule],                   // On l'utilisera pour simuler les réponses aux requêtes HTTP faites par le service
      providers: [SessionApiService],
    });
    service = TestBed.inject(SessionApiService);            // On initialise le service avec l'injecteur de test
    httpMock = TestBed.inject(HttpTestingController);       // On initialise le mock du contrôleur HTTP pour simuler les réponses HTTP
  });

  afterEach(() => {
    httpMock.verify();                                      // On s'assure qu'il n'y a pas de requêtes en attente après chaque test
  });


  // On vérifie que le service a bien été créé  (test unitaire )
  it('should be created', () => {
    expect(service).toBeTruthy();                           // On verifie qu'il répond    
  });


// On teste la récupération de toutes les sessions (test d'intégration )
  it('should retrieve all sessions', () => {
    service.all().subscribe(sessions => {                   // On appelle la méthode all() du service et on souscrit au résultat
      expect(sessions).toBeTruthy();                        // On vérifie simplement que la réponse est "truthy"
    });
    const req = httpMock.expectOne(service['pathService']);
    expect(req.request.method).toBe('GET');                 // On simule une réponse HTTP vide pour le test
    req.flush([]);                                          // On simule une réponse vide pour ce test
  });


  // On vérifie que le détail d'une session peut être récupéré par un ID  (test d'intégration )
  it('should retrieve session detail by id', () => {
    const testId = '123';                                                  // Création d'un ID fictif pour le test
    service.detail(testId).subscribe(session => {                          // On appelle la méthode detail() du service avec cet ID et souscrit au résultat
      expect(session).toBeTruthy();                                        // Vérifie que la réponse est "truthy"
    });
  
    const req = httpMock.expectOne(`${service['pathService']}/${testId}`); // On attend une requête HTTP à l'URL pour cet ID 
    expect(req.request.method).toBe('GET');                                // On vérifie que la méthode est bien GET
    req.flush({});                                                         // On simule une réponse vide pour ce test
  });


  // On définit un test pour vérifier que la méthode delete fonctionne
  it('should delete a session by id', () => {
    const testId = '123';                                                   // Création d'un ID fictif 
    service.delete(testId).subscribe(response => {                          // On appelle la méthode delete et on s'abonne à sa réponse
      expect(response).toBeNull();                                          // On vérifie que la réponse est bien Null
    });
  
    const req = httpMock.expectOne(`${service['pathService']}/${testId}`);  // On attend une requête HTTP DELETE de l'URL, basée sur l'ID créé plus haut
    expect(req.request.method).toBe('DELETE');                              // On vérifie que la méthode HTTP utilisée est bien DELETE
    req.flush(null);                                                        // On simule une réponse du serveur à la requête DELETE sans rien retourner
  });


  // Création d'un test pour vérifier que la méthode create fonctionne
  it('should create a session', () => {
    const newSession: Session = {                             // On crée un objet Session pour le test issu de la structure de l'interface Session importé plus haut
      id: 124,
      name: 'New Session',
      description: 'Description of the new session',
      date: new Date('2022-01-01T00:00:00Z'),
      teacher_id: 123,
      users: []
    };
  
    service.create(newSession).subscribe(session => {         // On appelle la méthode create avec le nouvel objet Session et on s'abonne à sa réponse
      expect(session).toEqual(newSession);                    // On vérifie que la session retournée par le serveur correspond à celle qui a été envoyée
    });
  
    const req = httpMock.expectOne(service['pathService']);    // On attend une requête HTTP POST à l'URL de base du service
    expect(req.request.method).toBe('POST');                   // On vérifie que la méthode HTTP utilisée est bien POST
    expect(req.request.body).toEqual(newSession);              // On vérifie que le corps de la requête POST correspond bien à l'objet Session créé plus tot
    req.flush(newSession);                                     // et on simule une réponse avec ce "newSession"
  });
  
  
  // Création d'un test pour vérifier que la méthode update fonctionne
  it('should update a session', () => {
    const updatedSession: Session = {
      id: 125,
      name: 'update Session',
      description: 'Description of the updatesession',
      date: new Date('2022-01-01T00:00:00Z'),
      teacher_id: 125,
      users: []
    };
    service.update(updatedSession.id!.toString(), updatedSession).subscribe(session => { // On appelle la méthode update avec l'ID et l'objet Session mis à jour, puis on s'abonne à sa réponse
      expect(session).toEqual(updatedSession);                                           // On vérifie que la session retournée par le serveur correspond à celle mise à jour
    });
  
    const req = httpMock.expectOne(`${service['pathService']}/${updatedSession.id}`);    // On attend une requête HTTP PUT à l'URL, avec l'ID de la session
    expect(req.request.method).toBe('PUT');                                              // On vérifie que la méthode HTTP utilisée est bien PUT
    expect(req.request.body).toEqual(updatedSession);                                    // On vérifie que le corps de la requête PUT correspond bien à l'objet Session que l'on vient de mettre à jour
    req.flush(updatedSession);                                                           // On simule une réponse du serveur avec l'objet Session mis à jour
  });
  

// On définit un test pour vérifier que la méthode participate fonctionne bien
  it('should allow a user to participate in a session', () => {
    const sessionId = '123';                                        // Création d'IDs fictifs pour la session pour le test
    const userId = 'user123';                                       // Création d'IDs fictifs pour l'utilisateur pour le test
    service.participate(sessionId, userId).subscribe(response => {  // On appelle la méthode participate et s'abonne à sa réponse
      expect(response).toBeUndefined();                             // On vérifie que la réponse est bien undefined
    });
  
    const req = httpMock.expectOne(`${service['pathService']}/${sessionId}/participate/${userId}`); // On attend une requête HTTP POST à l'URL pour l'opération de participation
    expect(req.request.method).toBe('POST');                        // On vérifie que la méthode HTTP utilisée est bien POST
    req.flush(null);                                                // On simule une réponse du serveur à la requête POST sans retourner de contenu
  });
  

  // On définit un test pour vérifier que la méthode unParticipate fonctionne bien
  it('should allow a user to unparticipate from a session', () => {
    const sessionId = '123';
    const userId = 'user123';
    service.unParticipate(sessionId, userId).subscribe(response => {  // On appelle la méthode unParticipate du service et on s'abonne à sa réponse.
      expect(response).toBeUndefined();                               // On vérifie que la réponse est bien undefined
    });
  
    const req = httpMock.expectOne(`${service['pathService']}/${sessionId}/participate/${userId}`); // On attend une requête HTTP DELETE à l'URL pour l'opération de désinscription.
    expect(req.request.method).toBe('DELETE');                        // Vérifie que la méthode HTTP utilisée est DELETE
    req.flush(null);                                                  // On simule une réponse du serveur à la requête DELETE sans retourner de contenu.
  });
  
});
