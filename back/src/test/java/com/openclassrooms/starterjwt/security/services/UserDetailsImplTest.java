package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    // Variables réutilisables pour les tests
    private final Long USER_ID = 1L;
    private final String USERNAME = "testuser";
    private final String PASSWORD = "openclassrooms";
    private final boolean IS_ADMIN = true;

    // Création d'un utilisateur de test
    private UserDetailsImpl createUserDetails(Long id, String username, boolean isAdmin) {
        return UserDetailsImpl.builder()
                .id(id)
                .username(username)
                .firstName("Test")
                .lastName("User")
                .admin(isAdmin)
                .password(PASSWORD)
                .build();
    }

    @Test
    void testGetAuthorities_ReturnsEmptySet() {
        // GIVEN: Création d'un UserDetails avec les droits d'administrateur
        UserDetailsImpl userDetails = createUserDetails(USER_ID, USERNAME, IS_ADMIN);

        // WHEN: Récupération des autorités de l'utilisateur
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // THEN: Vérification que la collection d'autorités est vide
        assertNotNull(authorities, "La collection d'autorités ne devrait pas être nulle.");
        assertTrue(authorities.isEmpty(), "La collection d'autorités devrait être vide.");
    }


    // On teste les propriétés qui retournent toujours vrai, démontrant que le compte est toujours actif et valide
    @Test
    void testIsAccountNonExpired_AlwaysReturnsTrue() {
        // GIVEN: Création d'un UserDetails vide
        UserDetailsImpl userDetails = createUserDetails(null, null, false);

        // WHEN & THEN: Vérification que le compte n'expire jamais
        assertTrue(userDetails.isAccountNonExpired(), "Le compte devrait toujours être considéré comme non expiré.");
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void testIsAccountNonLocked_AlwaysReturnsTrue() {
        // GIVEN: Création d'un UserDetails vide
        UserDetailsImpl userDetails = createUserDetails(null, null, false);
        assertTrue(userDetails.isAccountNonLocked(), "Le compte devrait toujours être considéré comme non verrouillé.");
    }

    @Test
    void testIsCredentialsNonExpired_AlwaysReturnsTrue() {
        // GIVEN: Création d'un UserDetails vide
        UserDetailsImpl userDetails = createUserDetails(null, null, false);
        assertTrue(userDetails.isCredentialsNonExpired(), "Les identifiants devraient toujours être considérés comme non expirés.");
    }

    @Test
    void testIsEnabled_AlwaysReturnsTrue() {
        // GIVEN: Création d'un UserDetails vide
        UserDetailsImpl userDetails = createUserDetails(null, null, false);
        assertTrue(userDetails.isEnabled(), "Le compte devrait toujours être considéré comme activé.");
    }

    @Test
    void testEquals_WithEqualObjects_ReturnsTrue() {
        // GIVEN: Deux instances UserDetails avec le même ID
        UserDetailsImpl userDetails1 = createUserDetails(USER_ID, USERNAME, IS_ADMIN);
        UserDetailsImpl userDetails2 = createUserDetails(USER_ID, USERNAME, IS_ADMIN);

        // THEN: Les deux instances devraient être considérées égales
        assertTrue(userDetails1.equals(userDetails2), "Deux objets UserDetails avec le même ID devraient être considérés comme égaux.");
    }

    @Test
    void testEquals_WithDifferentObjects_ReturnsFalse() {
        // GIVEN: Deux instances UserDetails avec des IDs différents
        UserDetailsImpl userDetails1 = createUserDetails(USER_ID, USERNAME, IS_ADMIN);
        UserDetailsImpl userDetails2 = createUserDetails(2L, "otheruser", IS_ADMIN);

        // THEN: Les deux instances ne devraient pas être considérées égales
        assertFalse(userDetails1.equals(userDetails2), "Deux objets UserDetails avec des IDs différents ne devraient pas être considérés comme égaux.");
    }

    @Test
    void testEquals_WithNullObject_ReturnsFalse() {
        // GIVEN: Une instance UserDetails et une référence null
        UserDetailsImpl userDetails = createUserDetails(USER_ID, USERNAME, IS_ADMIN);

        // THEN: UserDetails ne devrait pas être égal à null
        assertFalse(userDetails.equals(null), "UserDetails ne devrait pas être considéré comme égal à null.");
    }

    @Test
    void testEquals_WithDifferentClassObject_ReturnsFalse() {
        // GIVEN: Une instance UserDetails et un objet d'une autre classe
        UserDetailsImpl userDetails = createUserDetails(USER_ID, USERNAME, IS_ADMIN);

        // THEN: UserDetails ne devrait pas être égal à un objet d'une autre classe
        assertFalse(userDetails.equals(new Object()), "UserDetails ne devrait pas être considéré comme égal à un objet d'une autre classe.");
    }

    @Test
    void accountShouldNotBeExpired() {
        UserDetailsImpl userDetails = createUserDetails(USER_ID, USERNAME, IS_ADMIN);
        assertTrue(userDetails.isAccountNonExpired(), "Le compte ne devrait jamais expirer.");
    }

    @Test
    void accountShouldNotBeLocked() {
        UserDetailsImpl userDetails = createUserDetails(USER_ID, USERNAME, IS_ADMIN);
        assertTrue(userDetails.isAccountNonLocked(), "Le compte ne devrait jamais être verrouillé.");
    }

    @Test
    void credentialsShouldNotBeExpired() {
        UserDetailsImpl userDetails = createUserDetails(USER_ID, USERNAME, IS_ADMIN);
        assertTrue(userDetails.isCredentialsNonExpired(), "Les informations d'identification ne devraient jamais expirer.");
    }

    @Test
    void accountShouldBeEnabled() {
        UserDetailsImpl userDetails = createUserDetails(USER_ID, USERNAME, IS_ADMIN);
        assertTrue(userDetails.isEnabled(), "Le compte devrait toujours être activé.");
    }

    @Test
    void equalsShouldReturnTrueForSameId() {
        UserDetailsImpl userDetails = createUserDetails(USER_ID, USERNAME, IS_ADMIN);
        UserDetailsImpl anotherUserDetails = UserDetailsImpl.builder().id(USER_ID).build();
        assertEquals(userDetails, anotherUserDetails, "Deux instances avec le même ID devraient être considérées comme égales.");
    }

    @Test
    void equalsShouldReturnTrueForSameInstance() {
        UserDetailsImpl userDetails = createUserDetails(USER_ID, USERNAME, IS_ADMIN);
        assertTrue(userDetails.equals(userDetails), "Une instance devrait être considérée comme égale à elle-même.");
    }

    @Test
    void equalsShouldReturnFalseForDifferentId() {
        UserDetailsImpl userDetails = createUserDetails(USER_ID, USERNAME, IS_ADMIN);
        UserDetailsImpl anotherUserDetails = UserDetailsImpl.builder().id(2L).build();
        assertNotEquals(userDetails, anotherUserDetails, "Deux instances avec des ID différents ne devraient pas être considérées comme égales.");
    }

    @Test
    void equalsShouldReturnFalseForNull() {
        UserDetailsImpl userDetails = createUserDetails(USER_ID, USERNAME, IS_ADMIN);
        assertNotEquals(userDetails, null, "Une instance ne devrait pas être égale à null.");
    }

    @Test
    void equalsShouldReturnFalseForDifferentClass() {
        UserDetailsImpl userDetails = createUserDetails(USER_ID, USERNAME, IS_ADMIN);
        assertNotEquals(userDetails, new Object(), "Une instance ne devrait pas être égale à un objet d'une classe différente.");
    }

    @Test
    void testUserDetailsImplBuilderWithNullValues() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(null)
                .username(null)
                .firstName(null)
                .lastName(null)
                .admin(null)
                .password(null)
                .build();

        assertNull(userDetails.getId());
        assertNull(userDetails.getUsername());
        assertNull(userDetails.getFirstName());
        assertNull(userDetails.getLastName());
        assertNull(userDetails.getAdmin());
        assertNull(userDetails.getPassword());
    }


}
