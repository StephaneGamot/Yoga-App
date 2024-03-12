package com.openclassrooms.starterjwt.mapper.unit;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapperImpl;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class UserMapperUnitTest {
    // Variables réutilisables pour les tests
    private final Long VALID_USER_ID = 1L;
    private final String VALID_EMAIL = "test@example.com";
    private final String USER_LAST_NAME = "Dupond";
    private final String USER_FIRST_NAME = "Antoine";
    private final String USER_PASSWORD = "password";
    @InjectMocks
    private UserMapperImpl userMapper;

    private User generateUser() {
        return User.builder().id(VALID_USER_ID).email(VALID_EMAIL).lastName(USER_LAST_NAME).firstName(USER_FIRST_NAME).password(USER_PASSWORD).build();
    }

    private UserDto generateUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(VALID_USER_ID);
        userDto.setEmail(VALID_EMAIL);
        userDto.setLastName(USER_LAST_NAME);
        userDto.setFirstName(USER_FIRST_NAME);
        // Note: Le mot de passe n'est généralement pas inclus dans un DTO retourné
        return userDto;
    }

    @Test
    void whenConvertingUserEntityToDto_thenCorrect() {
        // given
        User user = generateUser();

        // when
        UserDto resultDto = userMapper.toDto(user);

        // then
        assertEquals(user.getId(), resultDto.getId());
        assertEquals(user.getEmail(), resultDto.getEmail());
        assertEquals(user.getLastName(), resultDto.getLastName());
        assertEquals(user.getFirstName(), resultDto.getFirstName());
    }


    @Test
    void whenConvertingUserEntityListToDtoList_thenCorrectSize() {
        // given
        List<User> entityList = Arrays.asList(generateUser(), generateUser());

        // when
        List<UserDto> dtoList = userMapper.toDto(entityList);

        // then
        assertEquals(entityList.size(), dtoList.size());
    }


    @Test
    void whenConvertingNullDtoListToEntityList_thenNull() {
        // given // when
        List<UserDto> dtoList = userMapper.toDto((List<User>) null);

        // then
        assertNull(dtoList);
    }

    @Test
    void whenConvertingNullDtoToEntity_thenNull() {
        // given // when
        User result = userMapper.toEntity((UserDto) null);

        // then
        assertNull(result);
    }

    @Test
    void whenConvertingNullEntityToDto_thenNull() {
        // given // when
        UserDto result = userMapper.toDto((User) null);

        // then
        assertNull(result);
    }


    @Test
    void whenConvertingUserDtoToEntityIncludingPassword_thenCorrect() {
        // given
        UserDto userDto = new UserDto();
        userDto.setId(VALID_USER_ID);
        userDto.setEmail(VALID_EMAIL);
        userDto.setLastName(USER_LAST_NAME);
        userDto.setFirstName(USER_FIRST_NAME);
        userDto.setPassword(USER_PASSWORD); // Include password in DTO for this test

        // when
        User result = userMapper.toEntity(userDto);

        // then
        // assertNotNull(result);
        assertEquals(userDto.getPassword(), result.getPassword(), "Password should be correctly mapped from DTO to entity.");
    }


    @Test
    void whenConvertingNullUserDtoToEntity_thenResultIsNull() {
        // given // when
        User result = userMapper.toEntity((UserDto) null);

        // then
        assertNull(result, "Converting a null UserDto to entity should result in null.");
    }

    @Test
    void whenConvertingNullEntityListToDtoList_thenResultIsNull() {
        // given // when
        List<UserDto> result = userMapper.toDto((List<User>) null);

        // then
        assertNull(result, "Converting a null entity list to DTO list should result in null.");
    }



}