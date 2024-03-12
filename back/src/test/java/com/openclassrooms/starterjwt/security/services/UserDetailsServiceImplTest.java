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
    private final String USER_EMAIL = "mario@example.com";
    private final String USER_PASSWORD = "password";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail(USER_EMAIL);
        testUser.setPassword(USER_PASSWORD);
        testUser.setFirstName("Mario");
        testUser.setLastName("Rossi");
    }

    @Test
    void whenLoadingUserByUsername_thenReturnsUserDetails() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername(USER_EMAIL);

        assertNotNull(userDetails, "L'utilisateur chargé ne devrait pas être nul.");
        assertEquals(USER_EMAIL, userDetails.getUsername(), "Le nom d'utilisateur devrait correspondre à l'email.");
        assertEquals(USER_PASSWORD, userDetails.getPassword(), "Le mot de passe devrait correspondre.");
    }

    @Test
    void whenLoadingNonExistentUserByUsername_thenThrowsUsernameNotFoundException() {
        String nonExistentEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(nonExistentEmail),
                "Devrait lancer une exception si l'utilisateur n'existe pas.");
    }

    @Test
    void givenUserFound_whenLoadingUserByUsername_thenReturnCorrectUserDetails() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername(USER_EMAIL);

        assertNotNull(userDetails, "Devrait retourner des détails d'utilisateur non nuls pour un utilisateur existant.");
        assertEquals(USER_EMAIL, userDetails.getUsername(), "L'email de l'utilisateur devrait être utilisé comme nom d'utilisateur.");
        assertEquals(USER_PASSWORD, userDetails.getPassword(), "Le mot de passe des détails de l'utilisateur devrait correspondre.");
    }

    @Test
    void givenUserNotFound_whenLoadingUserByUsername_thenThrowUsernameNotFoundException() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("unknown@example.com"),
                "Devrait lancer UsernameNotFoundException pour un email non trouvé.");
    }
}

