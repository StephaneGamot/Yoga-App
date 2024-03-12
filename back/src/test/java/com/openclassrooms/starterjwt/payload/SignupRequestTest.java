package com.openclassrooms.starterjwt.payload;


import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import org.junit.jupiter.api.Test;

public class SignupRequestTest {

    @Test
    public void testEquals() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");
        request1.setFirstName("Test");
        request1.setLastName("User");
        request1.setPassword("password");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");
        request2.setFirstName("Test");
        request2.setLastName("User");
        request2.setPassword("password");

        assertEquals(request1, request2, "Les deux objets SignupRequest devraient être considérés comme égaux");
    }

    @Test
    public void testHashCode() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setPassword("password");

        assertNotNull(request.hashCode(), "Le hashCode ne devrait pas être nul");
    }

    @Test
    public void testToString() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setPassword("password");

        String expectedString = "SignupRequest(email=test@example.com, firstName=Test, lastName=User, password=password)";
        assertEquals(expectedString, request.toString(), "La chaîne retournée devrait correspondre au format attendu");
    }



}
