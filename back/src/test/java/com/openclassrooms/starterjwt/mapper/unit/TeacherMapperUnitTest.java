package com.openclassrooms.starterjwt.mapper.unit;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapperImpl;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TeacherMapperUnitTest {
    // Variables réutilisables pour les tests
    private final Long TEACHER_ID_STEPHANE = 1L;
    private final Long TEACHER_ID_NATH = 2L;
    private final String LAST_NAME_GMT = "Gmt";
    private final String FIRST_NAME_STEPHANE = "Stéphane";
    private final String LAST_NAME_TAY = "Tay";
    private final String FIRST_NAME_NATH = "Nathalie";

    @InjectMocks
    private TeacherMapperImpl teacherMapper;


    // On vérifie que convertir un prof  'null' en un objet de transfert de données (DTO) retourne 'null'.
    @Test
    void convertingNullTeacherEntityToDtoShouldReturnNull() {
        // Given // When
        TeacherDto result = teacherMapper.toDto((Teacher) null);

        // ASSERT
        assertNull(result, "la conversion de la non existence d'un prof vers un dto doit retourner lui aussi Null");
    }


    // On vérifie que convertir un objet de transfert de données (DTO) en un prof  'null' et retourne 'null'.
    @Test
    void convertingNullTeacherDtoToEntityShouldReturnNull() {

        // Conversion d'un DTO 'null' en entité Teacher.
        Teacher result = teacherMapper.toEntity((TeacherDto) null);

        // Then ASSERT
        assertNull(result, "La convertion d'un prof inexistant ( dans le DTO) vers entity retourne bien un Null");
    }

    // On vérifie que lors d'une conversion d'une liste d'entités Teacher en une liste de DTOs la taille et le content reste identique
    @Test
    void convertingListOfTeacherEntitiesToShouldMatchInSizeAndContent() {
        // Given Création d'une liste d'entitées
        List<Teacher> entityList = Arrays.asList(
                Teacher.builder()
                        .id(TEACHER_ID_STEPHANE)
                        .lastName(LAST_NAME_GMT)
                        .firstName(FIRST_NAME_STEPHANE)
                        .build(),
                Teacher.builder()
                        .id(TEACHER_ID_NATH)
                        .lastName(LAST_NAME_TAY)
                        .firstName(FIRST_NAME_NATH)
                        .build());

        // When   On convertit cette liste d'entités en une liste de DTOs
        List<TeacherDto> dtoList = teacherMapper.toDto(entityList);

        // Then
        assertEquals(entityList.size(), dtoList.size(), "La taille de la liste convertit doit matcher avec la liste d'entity");

       // Création d'une boucle For qui permet d'iterer sur chaque element de chaque liste
        for (int i = 0; i < entityList.size(); i++) {
            assertEquals(entityList.get(i).getId(), dtoList.get(i).getId());
            assertEquals(entityList.get(i).getLastName(), dtoList.get(i).getLastName());
            assertEquals(entityList.get(i).getFirstName(), dtoList.get(i).getFirstName());
        }
    }

    @Test
    void convertingNullListOfTeacherEntitiesToShouldReturnNull() {
        // Given // When
        List<TeacherDto> dtoList = teacherMapper.toDto((List<Teacher>) null);

        // Then
        assertNull(dtoList, "La conversion d'une liste nulle d'entités de prof en DTOs doit  retourner un Null");
    }

    @Test
    void convertingNullListOfTeacherToEntitiesShouldReturnNull() {
        // Given // When
        List<Teacher> entityList = teacherMapper.toEntity((List<TeacherDto>) null);

        // Then
        assertNull(entityList, "La conversion d'une liste nulle de DTOs Enseignant en entités devrait retourner nul");
    }

    @Test
    void convertingCompleteTeacherDtoToEntityShouldMatch() {
        // Given
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(TEACHER_ID_STEPHANE);
        teacherDto.setLastName(LAST_NAME_GMT);
        teacherDto.setFirstName(FIRST_NAME_STEPHANE);
        // Assumer d'autres champs ici si nécessaire

        // When
        Teacher result = teacherMapper.toEntity(teacherDto);

        // Then
        assertNotNull(result);
        assertEquals(teacherDto.getId(), result.getId());
        assertEquals(teacherDto.getLastName(), result.getLastName());
        assertEquals(teacherDto.getFirstName(), result.getFirstName());
        // Assurer les assertions pour d'autres champs ici si nécessaire
    }

    @Test
    void convertingListOfTeacherDtosToEntitiesShouldReflectCorrectProperties() {
        // Given: Création de deux TeacherDto avec des propriétés spécifiques
        TeacherDto teacherDtoStephane = new TeacherDto();
        teacherDtoStephane.setId(TEACHER_ID_STEPHANE);
        teacherDtoStephane.setLastName(LAST_NAME_GMT);
        teacherDtoStephane.setFirstName(FIRST_NAME_STEPHANE);

        TeacherDto teacherDtoNathalie = new TeacherDto();
        teacherDtoNathalie.setId(TEACHER_ID_NATH);
        teacherDtoNathalie.setLastName(LAST_NAME_TAY);
        teacherDtoNathalie.setFirstName(FIRST_NAME_NATH);

        List<TeacherDto> dtoList = Arrays.asList(teacherDtoStephane, teacherDtoNathalie);

        // When: Conversion de la liste des TeacherDto en une liste des Teacher
        List<Teacher> entityList = teacherMapper.toEntity(dtoList);

        // Then: Vérifier que la taille des listes est identique et que les propriétés correspondent
        assertEquals(dtoList.size(), entityList.size(), "La taille des listes converties doit correspondre.");

        for (int i = 0; i < dtoList.size(); i++) {
            assertEquals(dtoList.get(i).getId(), entityList.get(i).getId(), "L'ID doit correspondre.");
            assertEquals(dtoList.get(i).getLastName(), entityList.get(i).getLastName(), "Le nom de famille doit correspondre.");
            assertEquals(dtoList.get(i).getFirstName(), entityList.get(i).getFirstName(), "Le prénom doit correspondre.");
        }
    }

}
