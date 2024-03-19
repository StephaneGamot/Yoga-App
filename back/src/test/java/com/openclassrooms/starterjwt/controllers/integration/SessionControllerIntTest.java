package com.openclassrooms.starterjwt.controllers.integration;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerIntTest {
    // URL de base pour les requêtes liées aux sessions
    private static final String SESSION_BASE_URL = "/api/session";
    private static final String N0T_A_NUMBER_ID = "a non numeric ID";
    private static final String ID_PATH = "/{id}";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SessionRepository sessionRepository;

    // Méthode utilitaire pour créer une session
    private Session createSession(String name) {
        Session session = new Session();
        session.setName(name);
        session.setDescription("mySession");
        session.setDate(Date.from(Instant.now()));
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());
        return session;
    }


    @Test
    @WithMockUser
    void testFindGetAllTheSessionsAndShouldReturnResponseOk() throws Exception {
        // Création  et sauvegarde des 2 sessions dans la base de données en préparation pour le test
        Session session1 = createSession("Stephane");
        Session session2 = createSession("Nathalie");
        sessionRepository.save(session1);
        sessionRepository.save(session2);

        // On stimule une requête HTTP GET vers l'URL de base des sessions, en spécifiant ce que l'on attend
        mockMvc.perform(MockMvcRequestBuilders.get(SESSION_BASE_URL).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
                //   .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(20)))

                // On utilise jsonPath pour naviguer dans le JSON et  les Matchers.hasItems pour vérifier les valeurs attendues.
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", Matchers.hasItems("Stephane", "Nathalie")));

    }

    @Test
    @WithMockUser
    void testFindByIdAndShouldReturnResponseOk() throws Exception {
        // given
        Session session = createSession("Stephane");

        sessionRepository.save(session);

        session = sessionRepository.save(session); // Sauvegarde pour obtenir un ID généré

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(SESSION_BASE_URL + ID_PATH, session.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Stephane"))).andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("mySession")));
    }

    @Test
    @WithMockUser
    void testFindByIdSessionDoesNotExistAndItShouldReturnResponseNotFound() throws Exception {

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(SESSION_BASE_URL + ID_PATH, Long.MAX_VALUE).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    void testFindByIdWithInvalidIdAndItShouldReturnBadRequest() throws Exception {

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(SESSION_BASE_URL + "/" + N0T_A_NUMBER_ID).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testCreateSessionAndItShouldReturnResponseOk() throws Exception {
        // given
        String newSessionJson = "{\"name\":\"Stephane\",\"date\":\"2024-03-10\",\"description\":\"mySession\",\"teacher_id\":1}";

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post(SESSION_BASE_URL).contentType(MediaType.APPLICATION_JSON).content(newSessionJson)).andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @WithMockUser
    void testDeleteSessionWithAnInvalidIdAndItShouldReturnBadRequest() throws Exception {

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete(SESSION_BASE_URL + ID_PATH, N0T_A_NUMBER_ID).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


}
