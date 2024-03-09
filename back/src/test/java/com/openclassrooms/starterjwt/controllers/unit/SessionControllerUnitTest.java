package com.openclassrooms.starterjwt.controllers.unit;
import com.openclassrooms.starterjwt.controllers.SessionController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.SessionService;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class SessionControllerUnitTest {

    // Création d'une instance du contrôleur à tester qui est injectée avec les dépendances mockées
    @InjectMocks
    private SessionController sessionController;

    // Mock des dépendances du sessionController
    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    // Définition des constantes pour les réutiliser dans les tests
    private static final Long SessionID1 = 1L;
    private static final Long SessionID2 = 2L;
    private static final String SessionFirstName = "Stéphane";
    private static final String SessionLastName = "Gmt";
    private static final String NonANumberID = "Not A Number ID";

    private Session session1, session2;
    private SessionDto sessionDto1, sessionDto2;

    @BeforeEach
    public void setup() {

        // On initialise des objets Session et SessionDto avec des valeurs
        session1 = Session.builder()
                .id(SessionID1)
                .name(SessionLastName)
                .date(new Date())
                .description("Session1")
                .teacher(Teacher.builder().id(SessionID1).build())
                .build();

        session2 = Session.builder()
                .id(SessionID2)
                .name(SessionFirstName)
                .date(new Date())
                .description("Session2")
                .teacher(Teacher.builder().id(SessionID2).build())
                .build();

        sessionDto1 = new SessionDto();
        sessionDto1.setId(SessionID1);
        sessionDto1.setName(SessionLastName);
        sessionDto1.setDate(new Date());
        sessionDto1.setDescription("Session1");
        sessionDto1.setTeacher_id(SessionID1);

        sessionDto2 = new SessionDto();
        sessionDto2.setId(SessionID2);
        sessionDto2.setName(SessionFirstName);
        sessionDto2.setDate(new Date());
        sessionDto2.setDescription("Session2");
        sessionDto2.setTeacher_id(SessionID2);
    }

    @Test
    public void findByIdWhenSessionExistsThenReturnsSession() {

        // On configure le comportement attendu du service et du mapper mockés
        given(sessionService.getById(SessionID1))
                .willReturn(session1);
        given(sessionMapper.toDto(session1))
                .willReturn(sessionDto1);

        // Act     On éxécute la méthode à tester
        ResponseEntity<?> response = sessionController.findById(SessionID1.toString());

        // ASSERT Et on vérifie que la réponse a le status OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void findByIdWhenSessionNotFoundReturnsNotFound() {

        // On configure pour que le service renvoie un 'null' si la session n'existe pas
        given(sessionService.getById(SessionID2))
                .willReturn(null);

        // Act     On éxécute la méthode à tester et on récupére de la réponse.
        ResponseEntity<?> response = sessionController.findById(SessionID2.toString());

        // ASSERT Et on vérifie que la réponse a le status Not Found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void findByIdWhenInvalidIdReturnsBadRequest() {

        // Act     On éxécute la méthode à tester avec un ID non numérique.
        ResponseEntity<?> response = sessionController.findById(NonANumberID);

        // ASSERT Et on vérifie que la réponse a le status Bad Request
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void getAllSessionsReturnsSessionsList() {

        // On configure le service pour retourner une liste contenant les deux sessions mockées
        given(sessionService.findAll()).willReturn(Arrays.asList(session1, session2));

        // Act     On éxécute la méthode 'findAll' à tester  et on récupére la réponse.
        ResponseEntity<?> response = sessionController.findAll();

        // ASSERT Et on vérifie que la réponse a le status OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void createSessionWhenValidInputReturnsOk() {

        // On configure le mapper pour convertir le DTO en entité et le service pour simuler la création de la session
        given(sessionMapper.toEntity(sessionDto1)).willReturn(session1);
        given(sessionService.create(session1)).willReturn(session1);

        // Act     On éxécute la méthode 'create' avec le DTO valide.
        ResponseEntity<?> response = sessionController.create(sessionDto1);

        // ASSERT Et on vérifie que la réponse a le status OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void updateSessionWhenValidInputReturnsOk() {

        // Configuration pour tester la mise à jour
        given(sessionMapper.toEntity(sessionDto1)).willReturn(session1);
        given(sessionService.update(SessionID1, session1)).willReturn(session1);

        // Act    On appel la methode update avec le DTO valide et l'ID de la session qui est mise à jour
        ResponseEntity<?> response = sessionController.update(SessionID1.toString(), sessionDto1);

        // ASSERT Et on vérifie que la réponse a le status OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void updateSessionWhenInvalidIdReturnsBadRequest() {

        // Act     On appel la méthode update avec un ID non numérique
        ResponseEntity<?> response = sessionController.update(NonANumberID, sessionDto1);

        // ASSERT Et on vérifie que la réponse a le status BadRequest
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void deleteSessionWhenSessionExistsReturnsOk() {

        // On configure un service pour indiquer que la session existe
        given(sessionService.getById(SessionID1)).willReturn(session1);

        // Act      On appel la méthode save ( pour la suppression) avec l'ID de la session qui est à supprimer
        ResponseEntity<?> response = sessionController.save(SessionID1.toString());

        // ASSERT Et on vérifie que la réponse a le status OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Vérification que la methode a bien été supprimé
        verify(sessionService).delete(SessionID1);
    }

    @Test
    public void deleteSessionWhenSessionNotFoundReturnsNotFound() {

        // On configure un service pour retourner 'null', nous indiquant que la session n'existe pas
        given(sessionService.getById(SessionID2)).willReturn(null);

        // Act     On appel la méthode save (pour la suppression) avec l'ID de la session inexistante
        ResponseEntity<?> response = sessionController.save(SessionID2.toString());

        // ASSERT Et on vérifie que la réponse a le status Not Found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteSessionWhenInvalidIdReturnsBadRequest() {

        // Act      On appel la méthode save (pour la suppression) avec un ID non numérique
        ResponseEntity<?> response = sessionController.save(NonANumberID);

        // ASSERT Et on vérifie que la réponse a le status 400
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void participateInSessionWhenValidIdsReturnsOk() {

        // On configure un service pour ne rien faire lors de l'appel à la méthode participate
        doNothing().when(sessionService).participate(SessionID1, SessionID1);

        // Act     On appel à la méthode participate avec des ID valides
        ResponseEntity<?> response = sessionController.participate(SessionID1.toString(), String.valueOf(SessionID1));

        // ASSERT Et on vérifie que la réponse a le status 200
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void participateInSessionWhenInvalidSessionIdReturnsBadRequest() {

        // Act    On appel la méthode participate avec un ID de session non numérique
        ResponseEntity<?> response = sessionController.participate(NonANumberID, String.valueOf(SessionID1));

        // ASSERT Et on vérifie que la réponse a le status 400
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void noLongerParticipateInSessionWhenValidIdsReturnsOk() {

        // On configure le service pour ne rien faire lors de l'appel à la méthode noLongerParticipate
        doNothing().when(sessionService).noLongerParticipate(SessionID1, SessionID1);

        // Act     On appel la méthode noLongerParticipate avec des ID valides
        ResponseEntity<?> response = sessionController.noLongerParticipate(SessionID1.toString(), String.valueOf(SessionID1));

        // ASSERT Et on vérifie que la réponse a le status 200
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void noLongerParticipateInSessionWhenInvalidSessionIdReturnsBadRequest() {

        // Act      On fait appel à la méthode noLongerParticipate avec un ID de session non numérique
        ResponseEntity<?> response = sessionController.noLongerParticipate(NonANumberID, String.valueOf(SessionID1));

        // ASSERT Et on vérifie que la réponse a le status 400
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}



