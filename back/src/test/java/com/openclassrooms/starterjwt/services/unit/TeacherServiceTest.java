package com.openclassrooms.starterjwt.services.unit;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {
    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private TeacherRepository teacherRepository;

    private final Long TEACHER_ID = 1L;
    private final String FIRST_NAME_MARIO = "Mario";
    private final String LAST_NAME_ROSSI = "Rossi";
    private final String FIRST_NAME_LUIGI = "Luigi";
    private final String LAST_NAME_VERDI = "Verdi";

    private Teacher createTeacher(Long id, String firstName, String lastName) {
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        return teacher;
    }

    @Test
    void whenFindingByIdAndTeacherExists_thenCorrect() {
        // GIVEN: Un enseignant existant dans le système
        Teacher mockTeacher = createTeacher(TEACHER_ID, FIRST_NAME_MARIO, LAST_NAME_ROSSI);
        when(teacherRepository.findById(TEACHER_ID)).thenReturn(Optional.of(mockTeacher));

        // WHEN: Recherche de cet enseignant par son ID
        Teacher foundTeacher = teacherService.findById(TEACHER_ID);

        // THEN: Vérification que l'enseignant est correctement retourné
        assertNotNull(foundTeacher, "L'enseignant trouvé ne devrait pas être nul.");
        assertEquals(FIRST_NAME_MARIO, foundTeacher.getFirstName(), "Le prénom de l'enseignant devrait correspondre.");
        assertEquals(LAST_NAME_ROSSI, foundTeacher.getLastName(), "Le nom de l'enseignant devrait correspondre.");
    }

    @Test
    void whenListingAllTeachers_thenCorrect() {
        // GIVEN: Une liste d'enseignants présents dans le système
        List<Teacher> mockTeachers = Arrays.asList(
                createTeacher(TEACHER_ID, FIRST_NAME_MARIO, LAST_NAME_ROSSI),
                createTeacher(2L, FIRST_NAME_LUIGI, LAST_NAME_VERDI)
        );
        when(teacherRepository.findAll()).thenReturn(mockTeachers);

        // WHEN: Récupération de tous les enseignants
        List<Teacher> allTeachers = teacherService.findAll();

        // THEN: Vérification que la liste contient les bons enseignants
        assertNotNull(allTeachers, "La liste des enseignants ne devrait pas être nulle.");
        assertEquals(2, allTeachers.size(), "La taille de la liste des enseignants devrait être de 2.");
        assertEquals(FIRST_NAME_MARIO, allTeachers.get(0).getFirstName(), "Le prénom du premier enseignant devrait correspondre.");
        assertEquals(LAST_NAME_VERDI, allTeachers.get(1).getLastName(), "Le nom du deuxième enseignant devrait correspondre.");
    }


    @Test
    void whenFindByIdWithInvalidId_thenReturnsNull() {
        // GIVEN: Un ID d'enseignant invalide pour lequel aucun enseignant ne correspond
        Long invalidTeacherId = 999L;
        when(teacherRepository.findById(invalidTeacherId)).thenReturn(Optional.empty());

        // WHEN: Tentative de recherche de l'enseignant par cet ID invalide
        Teacher result = teacherService.findById(invalidTeacherId);

        // THEN: Le résultat devrait être null, indiquant qu'aucun enseignant n'a été trouvé
        assertNull(result, "La recherche avec un ID invalide devrait retourner null.");
    }

    @Test
    void whenFindAllWithNoTeachers_thenReturnsEmptyList() {
        // GIVEN: Aucun enseignant présent dans le système
        when(teacherRepository.findAll()).thenReturn(new ArrayList<>());

        // WHEN: Récupération de tous les enseignants
        List<Teacher> allTeachers = teacherService.findAll();

        // THEN: La liste retournée devrait être vide
        assertTrue(allTeachers.isEmpty(), "La liste des enseignants devrait être vide si aucun enseignant n'est présent.");
    }



}
