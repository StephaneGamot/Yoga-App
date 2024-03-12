package com.openclassrooms.starterjwt.controllers.unit;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {

    // Définition des constantes pour les réutiliser dans les tests
    private static final String Email = "stephane@gmail.com";
    private static final String OtherEmail = "nathalie@gmail.com";
    private static final Long UserID = 1L;
    private static final String InvalidID = "invalid";

    // Mock des dépendances du UserController
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;

    // Création d'une instance du contrôleur à tester qui est injectée avec les dépendances mockées
    @InjectMocks
    private UserController userController;

    @Test
    public void findByIdIfUserExistsThenReturnUser() {

        // Création d'un nouvel utilisateur et d'un DTO pour les utiliser dans le test
        User user = new User();
        user.setId(UserID);
        UserDto userDto = new UserDto();

        // On configure le comportement des mocks. On simule les réponses attendues quand les méthodes sont appelées
        given(userService.findById(UserID))
                .willReturn(user);
        given(userMapper.toDto(user))
                .willReturn(userDto);

        // ACT On appelle la méthode à tester avec un ID valide et on capture la réponse.
        ResponseEntity<?> response = userController.findById(UserID.toString());

        // ASSERT  On vérifie que la réponse a le statut HTTP OK et que le corps de la réponse contient bien le DTO
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    public void findByIdIfUserDoesNotExistThenReturnNotFound() {

        // On configure le mock de userService pour simuler une situation dans laquelle il n'y aurait pas d'utilisateur
        given(userService.findById(UserID))
                .willReturn(null);

        // ACT  On appelle la méthode à tester avec l'ID qui n'existe pas
        ResponseEntity<?> response = userController.findById(UserID.toString());

        // ASSERT Et on vérifie que la réponse a le statut Not Found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void findByIdInvalidIdThenReturnBadRequest() {

        // ACT On appelle la méthode à tester avec un ID invalide (non numérique dans ce cas)
        ResponseEntity<?> response = userController.findById(InvalidID);

        // Assert et on vérifie que la réponse a pour status BAD_REQUEST
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void deleteUserIfUserExistsThenDeleteUser() {

        // Création d'un utilisateur pour ce test
        User user = new User();
        user.setId(UserID);
        user.setEmail(Email);

        // On simule le contexte d'authentification pour ce test
        UserDetails userDetails = mock(UserDetails.class);
        Authentication auth = mock(Authentication.class);

        // On configure les mocks pour simuler un utilisateur existant & un utilisateur authentifié avec le même email
        given(userService.findById(UserID))
                .willReturn(user);
        given(auth.getPrincipal())
                .willReturn(userDetails);
        given(userDetails.getUsername())
                .willReturn(Email);

        // Configuration du contexte de sécurité de Spring Security pour utiliser l'authentification mockée.
        SecurityContextHolder.getContext().setAuthentication(auth);

        // ACT On appelle la méthode à tester pour supprimer un utilisateur
        ResponseEntity<?> response = userController.save(UserID.toString());

        // ASSERT  On vérifie que la suppression a réussi et que le service correspondant a bien été appelé
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).delete(UserID);
    }

    @Test
    public void deleteUserIfUserDoesNotExistThenReturnNotFound() {

        // On configure le mock de userService pour simuler une situation où aucun utilisateur n'est trouvé
        given(userService.findById(UserID))
                .willReturn(null);

        // ACT On tente de supprimer un utilisateur qui n'existe pas
        ResponseEntity<?> response = userController.save(UserID.toString());

        // ASSERT Et on vérifie que la réponse a le status HTTP NOT_FOUND.
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteUserIfUserExistsButNotAuthorizedThenReturnUnauthorized() {

        // On prépare un utilisateur pour ce test
        User user = new User();
        user.setId(UserID);
        user.setEmail(Email);

        // On simule un contexte d'authentification avec un utilisateur authentifié autre que celui qui est à supprimer
        UserDetails userDetails = mock(UserDetails.class);
        Authentication auth = mock(Authentication.class);

        // On configure le comportement attendu des mocks
        given(userService.findById(UserID))
                .willReturn(user);
        given(auth.getPrincipal())
                .willReturn(userDetails);
        given(userDetails.getUsername())
                .willReturn(OtherEmail);

        // Configuration du contexte de sécurité de Spring Security pour utiliser l'authentification mockée
        SecurityContextHolder.getContext().setAuthentication(auth);

        // ACT On exécute la méthode save (ici delete ) du UserController avec l'ID de l'utilisateur qui est à supprimer
        ResponseEntity<?> response = userController.save(UserID.toString());

        // ASSERT on vérifie que la réponse a le statut HTTP "UNAUTHORIZED"
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void deleteUserIfInvalidIdThenReturnBadRequest() {

        // ACT on exécute la méthode save (utilisée pour la suppression) du UserController avec un ID invalide
        ResponseEntity<?> response = userController.save(InvalidID);

        // ASSERT On vérifie que la réponse a le statut HTTP "BAD_REQUEST"
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
/*
    @Test
    public void whenUserServiceThrowsException_thenResponseIsInternalServerError() {
        given(userService.findById(anyLong())).willThrow(new RuntimeException("Service exception"));

        ResponseEntity<?> response = userController.findById("1");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
*/



}
