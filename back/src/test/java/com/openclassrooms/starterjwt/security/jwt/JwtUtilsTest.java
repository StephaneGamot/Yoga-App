package com.openclassrooms.starterjwt.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.springframework.security.core.Authentication;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.Date;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    // Injection de la classe à tester avec les valeurs nécessaires pour les tests.
    @InjectMocks
    private JwtUtils jwtUtils = new JwtUtils();

    @Mock
    private Authentication authentication;

    // Configuration initiale pour les tests
    private final String jwtSecret = "openclassroomsSecret";
    private final int jwtExpirationMs = 3600000; // 1 heure exprimée en millisecondes
    private final String testUsername = "testuser";

    private String createValidToken(String username) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(nowMillis + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);
    }

    @Test
    void generateJwtToken_ValidAuthentication_GeneratesToken() {
        // Création d'un mock pour UserDetailsImpl avec des autorités vides
        UserDetailsImpl userPrincipal = Mockito.mock(UserDetailsImpl.class);
        when(userPrincipal.getUsername()).thenReturn(testUsername);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);

        // Génération du token
        String token = jwtUtils.generateJwtToken(authentication);

        // Assertions
        assertNotNull(token);
        assertNotEquals("", token.trim());
    }

    @Test
    void getUserNameFromJwtToken_ValidToken_ReturnsUsername() {
        String token = createValidToken(testUsername);

        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals(testUsername, extractedUsername, "Le nom d'utilisateur extrait devrait correspondre.");
    }

    @Test
    void validateJwtToken_ValidToken_ReturnsTrue() {
        String validToken = createValidToken(testUsername);

        assertTrue(jwtUtils.validateJwtToken(validToken), "Le token valide devrait être validé avec succès.");
    }

    @Test
    void validateJwtToken_InvalidSignature_ThrowsSignatureException() {
        // Génération d'un token avec une mauvaise clé secrète
        String token = Jwts.builder()
                .setSubject(testUsername)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, "wrongSecret")
                .compact();

        // Validation du token et attente d'une assertion de fausseté
        assertFalse(jwtUtils.validateJwtToken(token), "Un token avec une signature invalide ne devrait pas être validé.");
    }

    @Test
    void validateJwtToken_MalformedJwt_ThrowsMalformedJwtException() {
        // Utilisation d'un token qui n'est pas un JWT valide
        String token = "thisIsNotAValidJwt";

        // Validation du token et attente d'une assertion de fausseté
        assertFalse(jwtUtils.validateJwtToken(token), "Un token malformé ne devrait pas être validé.");
    }

    @Test
    void validateJwtToken_ExpiredToken_ThrowsExpiredJwtException() {
        // Génération d'un token expiré
        String token = Jwts.builder()
                .setSubject(testUsername)
                .setIssuedAt(new Date(System.currentTimeMillis() - 3600000)) // Temps d'émission dans le passé
                .setExpiration(new Date(System.currentTimeMillis() - 1800000)) // Temps d'expiration dans le passé
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // Validation du token et attente d'une assertion de fausseté
        assertFalse(jwtUtils.validateJwtToken(token), "Un token expiré ne devrait pas être validé.");
    }

    @Test
    void validateJwtToken_UnsupportedToken_ReturnsFalse() {
        // Génération d'un token sans signer pour simuler un token non supporté
        String unsupportedToken = Jwts.builder()
                .setSubject(testUsername)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                // Absence de la méthode signWith pour simuler un token non supporté
                .compact();

        // Validation du token et vérification que la méthode renvoie false
        assertFalse(jwtUtils.validateJwtToken(unsupportedToken), "Un token non supporté ne devrait pas être validé.");
    }


    @Test
    void validateJwtToken_EmptySubjectToken_ReturnsTrueAsCurrentBehavior() {
        // Génération d'un token avec une claim 'sub' (subject) vide pour simuler des claims vides
        String emptySubjectToken = Jwts.builder()
                .setSubject("") // Subject vide
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // Validation du token et vérification que la méthode renvoie true, reflétant le comportement actuel
        assertTrue(jwtUtils.validateJwtToken(emptySubjectToken), "Actuellement, un token avec un sujet vide est considéré comme valide.");
    }








}
