package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    // Variables réutilisables pour éviter les répétitions
    private final String validToken = "Bearer validToken123";
    private final String invalidToken = "Bearer invalidToken123";
    private final String username = "testUser";


    @Test
        // Si le token est valide, l'utilisateur doit être authentifié correctement
    void whenTokenIsValid_thenAuthenticateUser() throws ServletException, IOException {
        // GIVEN
        when(request.getHeader("Authorization")).thenReturn(validToken);
        when(jwtUtils.validateJwtToken(validToken.substring(7))).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(validToken.substring(7))).thenReturn(username);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // WHEN
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // THEN
        verify(userDetailsService).loadUserByUsername(username);
        verify(filterChain).doFilter(request, response);
    }

    @Test
        // Si le token n'est pas valide, l'authentification ne doit pas se faire.
    void whenTokenIsInvalid_thenDoNotAuthenticate() throws ServletException, IOException {
        // GIVEN
        when(request.getHeader("Authorization")).thenReturn(invalidToken);
        when(jwtUtils.validateJwtToken(invalidToken.substring(7))).thenReturn(false);

        // WHEN
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // THEN
        verify(filterChain).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
        // Si aucun token n'est fourni, le processus doit continuer sans essayer d'authentifier.
    void whenNoTokenProvided_thenProceedWithoutAuthentication() throws ServletException, IOException {
        // GIVEN
        when(request.getHeader("Authorization")).thenReturn(null);

        // WHEN
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // THEN
        verify(filterChain).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
        // Si la validation du token lance une exception, le processus doit quand même continuer.
    void whenTokenThrowsException_thenProceed() throws ServletException, IOException {
        // GIVEN
        when(request.getHeader("Authorization")).thenReturn(validToken);
        when(jwtUtils.validateJwtToken(validToken.substring(7))).thenThrow(new RuntimeException("Test exception"));

        // WHEN
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // THEN
        verify(filterChain).doFilter(request, response);

    }
}
