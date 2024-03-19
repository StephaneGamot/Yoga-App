package com.openclassrooms.starterjwt.controllers.unit;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthControllerUnitTest {

    // Définition des constantes pour les réutiliser dans les tests
    private static final Long UserId = 1L;
    private static final String Email = "stephane@gmail.com";
    private static final String Password = "Nathalie";
    private static final String FirstName = "Stephane";
    private static final String LastName = "Gmt";
    private static final boolean IsAdmin = false;
    private static final String JwtToken = "JwtToken";


    // Mock des dépendances du AuthController
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;

    private AuthController authController;      // Création d'une instance du contrôleur à tester qui est injectée avec les dépendances mockées


    // Avant chaque test, on initialise AuthController avec les dépendances mockées
    @BeforeEach
    public void setup() {
        authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
    }


    @Test
    public void loginUserOk() {
        // Création (ou contruction )de UserDetailsImpl mocké avec les valeurs constantes définies plus haut
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .username(Email)
                .firstName(FirstName)
                .lastName(LastName)
                .id(UserId)
                .password(Password)
                .build();

        // Création de 'AuthenticationToken mocké' qui permettra de simuler le processus d'authentification
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null);

        // Création de 'User mocké' qui permettra de simuler un utilisateur existant dans la base de données
        User user = User.builder()
                .id(UserId)
                .email(Email)
                .password(Password)
                .firstName(FirstName)
                .lastName(LastName)
                .admin(IsAdmin)
                .build();

        // On configure le comportement attendu des mocks lors de l'authentification
        given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(Email, Password)))
                .willReturn(authenticationToken);
        given(jwtUtils.generateJwtToken(authenticationToken))
                .willReturn(JwtToken);
        given(userRepository.findByEmail(Email))
                .willReturn(Optional.of(user));

        // On prépare la requête de login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(Email);
        loginRequest.setPassword(Password);

        // Act     On éxécute la méthode à tester
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);
        JwtResponse responseBody = (JwtResponse) response.getBody();

        // Assert   On vérifie les assertions pour s'assurer que la réponse est correcte
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getUsername()).isEqualTo(Email);
        assertThat(responseBody.getFirstName()).isEqualTo(FirstName);
        assertThat(responseBody.getLastName()).isEqualTo(LastName);
        assertThat(responseBody.getId()).isEqualTo(UserId);
        assertThat(responseBody.getAdmin()).isEqualTo(IsAdmin);
        assertThat(responseBody.getType()).isEqualTo("Bearer");
        assertThat(responseBody.getToken()).isNotNull();

    }

    @Test
    public void registerUserOk() {

        // On configure le comportement attendu des mocks pour le test d'enregistrement
        given(userRepository.existsByEmail(Email))
                .willReturn(false);
        given(passwordEncoder.encode(Password))
                .willReturn("hashedPassword");
        given(userRepository.save(any(User.class)))
                .willReturn(new User());

        // Arrange  On prépare la requête d'enregistrement
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail(Email);
        signUpRequest.setPassword(Password);
        signUpRequest.setFirstName(FirstName);
        signUpRequest.setLastName(LastName);


        // Act     On éxécute la méthode à tester
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Assert   On vérifie les assertions pour s'assurer que la réponse est correcte
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isInstanceOf(MessageResponse.class);
        MessageResponse messageResponse = (MessageResponse) response.getBody();     // On récupère le corps de la réponse après avoir vérifié son type
        assert messageResponse != null;
        assertThat(messageResponse.getMessage()).isEqualTo("User registered successfully!");

        // On vérifie que la méthode save a été appelée sur userRepository
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void registerEmailAlreadyBeenTaken() {

        // On configure le comportement attendu des mocks pour simuler un email qui est déjà pris
        given(userRepository.existsByEmail(Email))
                .willReturn(true);

        // Arrange  On prépare la requête d'enregistrement avec un email qui est déjà pris
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail(Email);
        signUpRequest.setPassword(Password);

        // Act     On éxécute la méthode à tester
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Assert   On vérifie les assertions pour s'assurer que la réponse est correcte
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(((MessageResponse) Objects.requireNonNull(response.getBody())).getMessage()).isEqualTo("Error: Email is already taken!");
    }

}