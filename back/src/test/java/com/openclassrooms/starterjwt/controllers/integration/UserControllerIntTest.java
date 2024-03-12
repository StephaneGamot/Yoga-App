package com.openclassrooms.starterjwt.controllers.integration;


import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntTest {

    // Constantes pour l'URL de l'API et les identifiants utilisés dans les tests
    public static final String API_USER_ID_URL = "/api/user/{id}";
    public static final Long VALID_USER_ID = 1L;
    public static final String INVALID_USER_ID = "nanID";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc; // MockMvc permet de simuler des requêtes HTTP

    // On teste si la récupération d'un utilisateur par ID valide renvoie bien le statut OK
    @Test
    @WithMockUser
    void getUserById_WhenIdIsValid_ThenReturnsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_USER_ID_URL, VALID_USER_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    // On teste si la récupération d'un utilisateur par un ID non numérique renvoie le statut BadRequest
    @Test
    @WithMockUser
    void getUserById_WhenIdIsNonNumeric_ThenReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_USER_ID_URL, INVALID_USER_ID))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // On teste si la récupération d'un utilisateur par un ID inexistant renvoie le statut NotFound
    @Test
    @WithMockUser
    void getUserById_WhenUserDoesNotExist_ThenReturnsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_USER_ID_URL, Long.MAX_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // On teste si la suppression d'un utilisateur sans authentification renvoie le statut Unauthorized
    @Test
    void deleteUserById_WhenUserIsNotAuthenticated_ThenReturnsUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(API_USER_ID_URL, VALID_USER_ID))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    // On teste si la suppression d'un utilisateur par un utilisateur authentifié sans les droits nécessaires renvoie Unauthorized
    @Test
    @WithMockUser(username = "testUser", password = "password", roles = "USER")
    void deleteUserById_WhenUserLacksAuthority_ThenReturnsUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(API_USER_ID_URL, VALID_USER_ID))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void whenSavedWithTooLongEmail_thenConstraintViolationException() {
        User user = new User()
                .setEmail("thisEmailIsWayTooLongToBeConsideredValid@example.com")
                .setPassword("password")
                .setFirstName("John")
                .setLastName("Doe")
                .setAdmin(false);

        assertThrows(ConstraintViolationException.class, () -> userRepository.saveAndFlush(user));
    }
}
