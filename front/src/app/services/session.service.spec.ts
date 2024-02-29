import { TestBed, fakeAsync, tick } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionInformation } from '../interfaces/sessionInformation.interface';
import { SessionService } from './session.service';

describe('SessionService', () => {

  let service: SessionService;


  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should handle login correctly', done => { 
    const testUser: SessionInformation = { id: 112 , username: 'testUser', token: "token", type: "type", firstName:'stéphane',lastName:'Gmt',admin: true }; 
    service.logIn(testUser);
    
    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(true);
      expect(service.sessionInformation).toEqual(testUser);
      done(); 
    });
  });

  it('should handle logout correctly', done => { 
    service.logOut();
    
    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(false);
      expect(service.sessionInformation).toBeUndefined();
      done(); 
    });
  });


  it('should correctly log in and update session information', () => {
    const testUser: SessionInformation = { id: 112 , username: 'testUser', token: "token", type: "type", firstName:'stéphane',lastName:'Gmt',admin: true }; 
    service.logIn(testUser);
  
    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(testUser);
  
  });
  

});
