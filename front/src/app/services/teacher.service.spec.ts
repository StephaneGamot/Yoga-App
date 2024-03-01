import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {                                                        // Après chaque test 
    httpMock.verify();                                                     // Cela s'assure qu'il n'y a pas de requêtes en suspens ou inattendues et que pas d'effets secondaires.
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });


  it('should retrieve teacher detail by id', () => {
    const testId = 1;                                                      // ID fictif pour le test
    const mockTeacher: Teacher = {                                         // On crée un Mock d'une instance de l'objet Teacher pour le test.
      id: testId,
      firstName: 'teacher firstname',
      lastName: 'teacher lastname',
      createdAt: new Date('2022-01-01T00:00:00Z'),
      updatedAt: new Date('2022-01-01T00:00:00Z'),
    };

    service.detail(testId.toString()).subscribe((teacher) => {             // On appelle la méthode detail et on s'abonne à sa réponse
      expect(teacher).toEqual(mockTeacher);                                // On vérifie que la réponse correspond à l'objet mockTeacher
    });

    const req = httpMock.expectOne(`${service['pathService']}/${testId}`); // On attend une requête HTTP GET à l'URL avec l'ID du professeur
    expect(req.request.method).toBe('GET');                                // On attend une requête HTTP GET basée sur l'URL construite avec l'ID du prof.
    req.flush(mockTeacher);                                                // Et on simule une réponse
  });
});
