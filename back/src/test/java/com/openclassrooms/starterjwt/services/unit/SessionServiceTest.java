package com.openclassrooms.starterjwt.services.unit;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        // Créer et retourner une instance de Session pour les tests
        Session session = new Session();
        session.setId(VALID_SESSION_ID);
        session.setName(SESSION_NAME);
        session.setDate(SESSION_DATE);
        session.setDescription(SESSION_DESCRIPTION);
        session.setTeacher(new Teacher()); // Assurez-vous que le Teacher est configuré correctement
        session.setUsers(new ArrayList<>()); // Commencez avec une liste d'utilisateurs vide
        return session;
    }

    @Test
    void whenCreatingASession_thenTheSessionIsSuccessfullySaved() {
        // Configuration
        Session newSession = createTestSession();
        when(sessionRepository.save(any(Session.class))).thenReturn(newSession);

        // Action
        Session savedSession = sessionService.create(newSession);

        // Vérification
        assertNotNull(savedSession, "La session sauvegardée ne devrait pas être null.");
        assertEquals(SESSION_NAME, savedSession.getName(), "Le nom de la session devrait correspondre.");
    }

    @Test
    void whenDeletingASession_thenTheRepositoryDeleteMethodIsCalled() {
        // Action
        sessionService.delete(VALID_SESSION_ID);

        // Vérification
        verify(sessionRepository).deleteById(VALID_SESSION_ID);
    }

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

    // Continuez avec les autres méthodes de test en suivant ce modèle.
}
