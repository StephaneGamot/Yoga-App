package com.openclassrooms.starterjwt.controllers.integration;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    // Définir l'URL de base pour les requêtes liées aux enseignants
    private static final String TEACHER_BASE_URL = "/api/teacher";

    @BeforeEach
    public void setup() {

        // Création des profs pour ces tests
        Teacher teacher1 = createTeacher("Tiffa", "juuig");
        Teacher teacher2 = createTeacher("John", "molitier");

        // Sauvegarde dans la base de données pour les tests qui suivent
        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
    }

    // Méthode utilitaire qui permet de créer un un prof
    private Teacher createTeacher(String firstName, String lastName) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());
        return teacher;
    }

    /*
    @Test
    @WithMockUser
    void testMustFindAllTeachersAndShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(TEACHER_BASE_URL)  // On simule une requête HTTP GET vers l'URL définie
                        .contentType(MediaType.APPLICATION_JSON))     // En spécifiant que le type de contenu attendu est APPLICATION_JSON
                .andExpect(MockMvcResultMatchers.status().isOk())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", Matchers.is("Tiffa")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName", Matchers.is("juuig")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName", Matchers.is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName", Matchers.is("molitier")));
    }
*/
    @Test
    @WithMockUser
    void testFindByIdShouldReturnResponseOk() throws Exception {
        // On ajoute un prof avec un ID spécifique pour ce test
        Teacher teacher = createTeacher("Nath", "Tay");
        teacher = teacherRepository.save(teacher);

       // On simule une requête HTTP GET pour trouver un prof par ID, en utilisant l'ID du prof sauvegardé
        mockMvc.perform(MockMvcRequestBuilders.get(TEACHER_BASE_URL + "/{id}", teacher.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is("Nath")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is("Tay")));
    }


    // On teste la requête pour trouver un enseignant par un ID qui n'existe pas et va donc retourner un status 404
    @Test
    @WithMockUser
    void testFindByIdShouldReturnNotFoundCode() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(TEACHER_BASE_URL + "/{id}", Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

