package com.openclassrooms.starterjwt.services.unit;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    UserRepository userRepository;

    // Variables communes pour améliorer la lisibilité
    private final Long EXISTING_USER_ID = 1L;
    private final Long NON_EXISTING_USER_ID = 2L;
    private final String USER_EMAIL = "test@example.com";

    private User createTestUser(Long id, String email) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        return user;
    }

    @Test
    void deleteUser_ShouldCallUserRepositoryDeleteById() {
        // GIVEN: Un ID utilisateur pour un test de suppression
        Long userId = EXISTING_USER_ID;

        // WHEN: Appel de la méthode de suppression sur le service
        userService.delete(userId);

        // THEN: Vérification que la méthode deleteById du repository a été appelée une fois avec l'ID donné
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void findById_UserExists_ShouldReturnUser() {
        // GIVEN: Un utilisateur existant simulé retourné par le repository
        User mockUser = createTestUser(EXISTING_USER_ID, USER_EMAIL);
        when(userRepository.findById(EXISTING_USER_ID)).thenReturn(Optional.of(mockUser));

        // WHEN: Recherche de l'utilisateur par son ID
        User resultUser = userService.findById(EXISTING_USER_ID);

        // THEN: Vérification que l'utilisateur est bien retourné et correspond aux attentes
        assertNotNull(resultUser, "L'utilisateur trouvé ne devrait pas être nul");
        assertEquals(EXISTING_USER_ID, resultUser.getId(), "L'ID de l'utilisateur devrait correspondre à l'ID recherché");
        assertEquals(USER_EMAIL, resultUser.getEmail(), "L'email de l'utilisateur devrait correspondre à l'email attendu");
    }

    @Test
    void findById_UserNotExists_ShouldReturnNull() {
        // GIVEN: Aucun utilisateur n'est trouvé pour l'ID donné
        when(userRepository.findById(NON_EXISTING_USER_ID)).thenReturn(Optional.empty());

        // WHEN: Tentative de recherche d'un utilisateur inexistant
        User resultUser = userService.findById(NON_EXISTING_USER_ID);

        // THEN: Vérification que null est retourné, indiquant qu'aucun utilisateur n'a été trouvé
        assertNull(resultUser, "Aucun utilisateur ne devrait être retourné pour un ID inexistant");
    }
}
