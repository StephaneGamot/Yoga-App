package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    private final Long VALID_SESSION_ID = 1L;
    private final String SESSION_NAME = "Intro to Spring";
    private final Date SESSION_DATE = new Date();
    private final String SESSION_DESCRIPTION = "An introductory session on Spring Framework";
    private final Long VALID_USER_ID = 2L;
    @InjectMocks
    private SessionService sessionService;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UserRepository userRepository;



    private Session createTestSession() {
        // Création d'une instance de Session pour les tests
        Session session = new Session();
        session.setId(VALID_SESSION_ID);
        session.setName(SESSION_NAME);
        session.setDate(SESSION_DATE);
        session.setDescription(SESSION_DESCRIPTION);
        session.setTeacher(new Teacher());
        session.setUsers(new ArrayList<>());
        return session;
    }


    // On teste si une nouvelle session est correctement sauvegardée via le service
    @Test
    void whenCreatingASession_thenTheSessionIsSuccessfullySaved() {
        // Configuration
        Session newSession = createTestSession();
        when(sessionRepository.save(any(Session.class))).thenReturn(newSession);

        // Action
        Session savedSession = sessionService.create(newSession);

        // ASSERT
        assertNotNull(savedSession, "La session sauvegardée ne devrait pas être null.");
        assertEquals(SESSION_NAME, savedSession.getName(), "Le nom de la session devrait correspondre.");
    }


    // On teste si le service appelle correctement la méthode deleteById du repository pour supprimer une session
    @Test
    void whenDeletingASession_thenTheRepositoryDeleteMethodIsCalled() {
        // Action
        sessionService.delete(VALID_SESSION_ID);

        // Vérification
        verify(sessionRepository).deleteById(VALID_SESSION_ID);
    }


    // On teste si le service peut récupérer et retourner toutes les sessions existante
    @Test
    void whenRetrievingAllSessions_thenAllSessionsAreReturned() {
        // Configuration
        List<Session> expectedSessions = Arrays.asList(createTestSession(), createTestSession());
        when(sessionRepository.findAll()).thenReturn(expectedSessions);

        // Action
        List<Session> allSessions = sessionService.findAll();

        // Vérification
        assertEquals(expectedSessions.size(), allSessions.size(), "Le nombre de sessions retournées devrait correspondre.");
    }


    // On teste si le service est capable de récupérer une session spécifique par son ID
    @Test
    void whenGettingSessionById_thenCorrectSessionIsReturned() {
        // Configuration
        Session expectedSession = createTestSession();
        when(sessionRepository.findById(VALID_SESSION_ID)).thenReturn(Optional.of(expectedSession));

        // Action
        Session session = sessionService.getById(VALID_SESSION_ID);

        // Vérification
        assertNotNull(session, "La session récupérée ne devrait pas être null.");
        assertEquals(SESSION_NAME, session.getName(), "Le nom de la session devrait correspondre.");
    }


    // On teste si une session peut être mise à jour correctement
    @Test
    void whenUpdatingASession_thenTheSessionIsUpdated() {
        // Configuration
        Session sessionToUpdate = createTestSession();
        when(sessionRepository.save(any(Session.class))).thenReturn(sessionToUpdate);

        // Action
        Session updatedSession = sessionService.update(VALID_SESSION_ID, sessionToUpdate);

        // Vérification
        assertNotNull(updatedSession, "La session mise à jour ne devrait pas être null.");
        assertEquals(SESSION_DESCRIPTION, updatedSession.getDescription(), "La description de la session devrait correspondre.");
    }

    // Tester le retrait d'un utilisateur d'une session
    @Test
    void whenRemovingUserFromSession_thenUserIsRemoved() {
        // Configuration
        Session session = createTestSession();
        User user = new User(); // Assumez une méthode helper pour créer un utilisateur test
        user.setId(VALID_USER_ID);
        session.getUsers().add(user);
        when(sessionRepository.findById(VALID_SESSION_ID)).thenReturn(Optional.of(session));

        // Action
        assertDoesNotThrow(() -> sessionService.noLongerParticipate(VALID_SESSION_ID, VALID_USER_ID));

        // Vérification
        assertFalse(session.getUsers().contains(user), "L'utilisateur devrait être retiré de la session.");
    }

    // Tester la tentative de retrait d'un utilisateur d'une session non existante
    @Test
    void whenRemovingUserFromNonExistentSession_thenThrowsNotFoundException() {
        // Configuration
        when(sessionRepository.findById(VALID_SESSION_ID)).thenReturn(Optional.empty());

        // Action & Vérification
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(VALID_SESSION_ID, VALID_USER_ID));
    }

    // Tester la tentative de retrait d'un utilisateur qui ne participe pas à la session
    @Test
    void whenRemovingNonParticipantUser_thenThrowsBadRequestException() {
        // Configuration
        Session session = createTestSession(); // La session ne contient pas l'utilisateur
        when(sessionRepository.findById(VALID_SESSION_ID)).thenReturn(Optional.of(session));

        // Action & Vérification
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(VALID_SESSION_ID, VALID_USER_ID));
    }



    // Tester la gestion des données invalides lors de la création d'une session
    @Test
    void whenCreatingSessionWithInvalidData_thenBehaviorIsAsExpected() {
        // Configuration
        Session invalidSession = new Session(); // Assumez une session invalide (ex: sans nom)

        // Action
        Session result = sessionService.create(invalidSession);

        // Vérification
        assertNull(result, "La méthode devrait retourner null pour des données invalides.");

    }

    @Test
    void whenAddingUserToSession_thenUserIsAddedSuccessfully() {
        // Configuration
        Session session = createTestSession();
        User newUser = new User();
        newUser.setId(3L); // ID différent pour le nouveau utilisateur
        when(sessionRepository.findById(VALID_SESSION_ID)).thenReturn(Optional.of(session));
        when(userRepository.findById(3L)).thenReturn(Optional.of(newUser));

        // Action
        assertDoesNotThrow(() -> sessionService.participate(VALID_SESSION_ID, 3L));

        // Vérification
        assertTrue(session.getUsers().contains(newUser), "Le nouvel utilisateur devrait être ajouté à la session.");
    }


    @Test
    void whenGettingSessionById_thenSessionContainsExpectedUsers() {
        // Configuration
        Session sessionWithUsers = createTestSession();
        User participatingUser = new User();
        participatingUser.setId(VALID_USER_ID);
        sessionWithUsers.getUsers().add(participatingUser);
        when(sessionRepository.findById(VALID_SESSION_ID)).thenReturn(Optional.of(sessionWithUsers));

        // Action
        Session resultSession = sessionService.getById(VALID_SESSION_ID);

        // Vérification
        assertTrue(resultSession.getUsers().contains(participatingUser), "La session récupérée devrait contenir les utilisateurs participants.");
    }


}
