package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    private User testUser;
    private final String USER_EMAIL = "stephane@gmail.com";
    private final String USER_PASSWORD = "password";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail(USER_EMAIL);
        testUser.setPassword(USER_PASSWORD);
        testUser.setFirstName("Stéphane");
        testUser.setLastName("Gmt");
    }


    // On teste le chargement d'un utilisateur par son nom d'utilisateur (email) quand il existe dans le système.
    @Test
    void whenLoadingUserByUsername_thenReturnsUserDetails() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(testUser));

        // Charge les détails de l'utilisateur par son email
        UserDetails userDetails = userDetailsService.loadUserByUsername(USER_EMAIL);

        assertNotNull(userDetails, "L'utilisateur chargé ne devrait pas être nul.");
        assertEquals(USER_EMAIL, userDetails.getUsername(), "Le nom d'utilisateur devrait correspondre à l'email.");
        assertEquals(USER_PASSWORD, userDetails.getPassword(), "Le mot de passe devrait correspondre.");
    }


    // On  teste la tentative de chargement d'un utilisateur qui n'existe pas par son nom d'utilisateur
    @Test
    void whenLoadingNonExistentUserByUsername_thenThrowsUsernameNotFoundException() {

        // On configure le UserRepository pour ne retourner aucun utilisateur pour un email donné.
        String nonExistentEmail = "emailquinexistepas@example.com";
        when(userRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

        // On essaie de charger les détails de l'utilisateur et vérifie qu'une exception est bien lancée.
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(nonExistentEmail),
                "Devrait lancer une exception si l'utilisateur n'existe pas.");
    }


    // On teste le chargement réussi des détails d'un utilisateur par son nom d'utilisateur quand l'utilisateur existe bien
    @Test
    void givenUserFound_whenLoadingUserByUsername_thenReturnCorrectUserDetails() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(testUser));

        // Charge l'utilisateur et vérifie que les informations retournées sont correctes
        UserDetails userDetails = userDetailsService.loadUserByUsername(USER_EMAIL);

        assertNotNull(userDetails, "Devrait retourner des détails d'utilisateur non nuls pour un utilisateur existant.");
        assertEquals(USER_EMAIL, userDetails.getUsername(), "L'email de l'utilisateur devrait être utilisé comme nom d'utilisateur.");
        assertEquals(USER_PASSWORD, userDetails.getPassword(), "Le mot de passe des détails de l'utilisateur devrait correspondre.");
    }


    // On teste la tentative de chargement d'un utilisateur par son nom d'utilisateur quand cet utilisateur n'existe pas
    @Test
    void givenUserNotFound_whenLoadingUserByUsername_thenThrowUsernameNotFoundException() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());


        // On tente de charger l'utilisateur par l'email et vérifie qu'une exception UsernameNotFoundException est lancée
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("unknown@example.com"),
                "Devrait lancer UsernameNotFoundException pour un email non trouvé.");
    }
}

