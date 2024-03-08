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
import static org.mockito.BDDMockito.given;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerUnitTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;
    private AuthController authController;

    private static final Long UserId = 1L;
    private static final String Email = "stephane@gmail.com";
    private static final String Password = "Nathalie";
    private static final String FirstName = "Stephane";
    private static final String LastName = "Gmt";
    private static final boolean IsAdmin = false;

    @BeforeEach
    public void setup() {
        // Initialize the AuthController with the mocked dependencies
        authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
    }


    @Test
    public void loginUserOk() {

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .username(Email)
                .firstName(FirstName)
                .lastName(LastName)
                .id(UserId)
                .password(Password)
                .build();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null);

        User user = User.builder()
                .id(UserId)
                .email(Email)
                .password(Password)
                .firstName(FirstName)
                .lastName(LastName)
                .admin(IsAdmin)
                .build();

        given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(Email, Password))).willReturn(authenticationToken);
        given(jwtUtils.generateJwtToken(authenticationToken)).willReturn("JwtToken");
        given(userRepository.findByEmail(Email)).willReturn(Optional.of(user));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(Email);
        loginRequest.setPassword(Password);

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);
        JwtResponse responseBody = (JwtResponse) response.getBody();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Email, responseBody.getUsername());
        assertEquals(FirstName, responseBody.getFirstName());
        assertEquals(LastName, responseBody.getLastName());
        assertEquals(UserId, responseBody.getId());
        assertEquals(IsAdmin, responseBody.getAdmin());
        assertEquals("Bearer", responseBody.getType());
        assertNotNull(responseBody.getToken());
    }

    @Test
    public void registerUserOk() {

        given(userRepository.existsByEmail(Email)).willReturn(false);
        given(passwordEncoder.encode(Password)).willReturn("hashedPassword");
        given(userRepository.save(any(User.class))).willReturn(new User());

        // Arrange
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail(Email);
        signUpRequest.setPassword(Password);
        signUpRequest.setFirstName(FirstName);
        signUpRequest.setLastName(LastName);


        // Act
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());

        // On vérifie que save() a été appelé sur userRepository
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void registerEmailAlreadyBeenTaken () {
        given(userRepository.existsByEmail(Email)).willReturn(true);

        // Arrange
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail(Email);
        signUpRequest.setPassword(Password);

        // Act
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Email is already taken!", ((MessageResponse) response.getBody()).getMessage());

    }

}