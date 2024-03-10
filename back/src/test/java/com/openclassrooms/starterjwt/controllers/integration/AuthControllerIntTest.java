package com.openclassrooms.starterjwt.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // On l'utilise pour convertir les objets en chaîne JSON

    private static final String REGISTER_URL = "/api/auth/register";

    // On teste l'enregistrement réussi d'un nouvel utilisateur
  /*  @Test
    public void whenRegisteringValidUser_thenReturnsSuccessMessage() throws Exception {
        // Création d'une nouvelle requête d'inscription avec des données valides
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser3@example.com");
        signupRequest.setPassword("Test1234!");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");

        // On simule une requête POST vers /api/auth/register avec le corps de la requête converti en JSON
        mockMvc.perform(MockMvcRequestBuilders.post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)                   // On définit le type de contenu de la requête
                        .content(objectMapper.writeValueAsString(signupRequest)))  // On convertit l'objet de requête en chaîne JSON
                .andExpect(MockMvcResultMatchers.status().isOk())                  // On vérifie que le statut HTTP de la réponse est 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User registered successfully!")); // On vérifie que le message dans le corps de la réponse correspond bien à l'attente
    }
*/
    @Test
    public void whenRegisteringUserWithExistingEmail_thenReturnsBadRequest() throws Exception {
        // Préparation de la requête d'inscription avec une adresse email déjà existante dans la base de donnée
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("existinguser@example.com"); // Cette adresse e-mail est déjà utilisée dans la base de données
        signupRequest.setPassword("Test1234!");
        signupRequest.setFirstName("Jane");
        signupRequest.setLastName("Doe");

        // Simule une requête POST vers /api/auth/register avec des données d'inscription qui devraient échouer
        mockMvc.perform(MockMvcRequestBuilders.post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error: Email is already taken!"));
    }

}