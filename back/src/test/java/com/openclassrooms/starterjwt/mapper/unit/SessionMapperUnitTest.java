package com.openclassrooms.starterjwt.mapper.unit;


import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapperImpl;
import com.openclassrooms.starterjwt.models.Session;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SessionMapperUnitTest {


    // Variables réutilisables
    private final Long SESSION_ID_1 = 1L;
    private final String SESSION_NAME_1 = "Session 1";
    @InjectMocks
    private SessionMapperImpl sessionMapper;


    // Définition d'une méthode utilitaire pour créer des objets SessionDto.
    private SessionDto createSessionDto(Long id, String name) {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(id);
        sessionDto.setName(name);

        return sessionDto;
    }

    private Session createSession(Long id, String name) {
        // Créez une instance de votre entité Session ici. Adaptez cette méthode en fonction de vos attributs réels.
        Session session = new Session();
        session.setId(id);
        session.setName(name);

        return session;
    }

    // Test pour vérifier que la conversion d'un objet SessionDto valide l'entité Session
    @Test
    void whenConvertingValidSessionDtoToEntity_expectMatchingEntity() {
        SessionDto sessionDto = createSessionDto(SESSION_ID_1, SESSION_NAME_1);

        Session result = sessionMapper.toEntity(sessionDto);

        assertNotNull(result);
        assertEquals(sessionDto.getId(), result.getId());
        assertEquals(sessionDto.getName(), result.getName());
    }


    // Test pour vérifier que la conversion d'une liste d'objets SessionDto en une liste d'entités Session se fait correctement
    @Test
    void whenConvertingValidSessionDtoListToEntityList_expectMatchingEntityList() {
        List<SessionDto> dtoList = Arrays.asList(createSessionDto(SESSION_ID_1, SESSION_NAME_1), createSessionDto(2L, "Session 2"));

        List<Session> entityList = sessionMapper.toEntity(dtoList);

        assertEquals(dtoList.size(), entityList.size());
        assertEquals(dtoList.get(0).getId(), entityList.get(0).getId());
        assertEquals(dtoList.get(1).getName(), entityList.get(1).getName());
    }
    @Test
    void whenConvertingNullSessionDtoToEntity_expectNull() {
        assertNull(sessionMapper.toEntity((SessionDto) null), "Converting a null SessionDto to entity should return null.");
    }



    @Test
    void whenConvertingValidSessionToDto_expectMatchingDto() {
        Session session = createSession(SESSION_ID_1, SESSION_NAME_1);

        SessionDto resultDto = sessionMapper.toDto(session);

        assertNotNull(resultDto);
        assertEquals(session.getId(), resultDto.getId());
        assertEquals(session.getName(), resultDto.getName());
    }

    @Test
    void whenConvertingValidSessionListToDtoList_expectMatchingDtoList() {
        List<Session> sessionList = Arrays.asList(createSession(SESSION_ID_1, SESSION_NAME_1), createSession(2L, "Session 2"));

        List<SessionDto> resultDtoList = sessionMapper.toDto(sessionList);

        assertEquals(sessionList.size(), resultDtoList.size());
        assertEquals(sessionList.get(0).getId(), resultDtoList.get(0).getId());
        assertEquals(sessionList.get(1).getName(), resultDtoList.get(1).getName());
    }

    @Test
    void whenConvertingNullSessionToDto_expectNull() {
        assertNull(sessionMapper.toDto((Session) null), "Converting a null Session to Dto should return null.");
    }


}
