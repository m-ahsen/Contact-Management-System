package com.ahsen.contactmanagement.auth;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ahsen.contactmanagement.common.ApiConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import tools.jackson.databind.json.JsonMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper objectMapper;

    @Test
    void registerWithEmailReturnsCreated() throws Exception {
        mockMvc.perform(post(ApiConstants.AUTH + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "email-user@example.com",
                                "password", "Password1"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void registerWithPhoneReturnsCreated() throws Exception {
        mockMvc.perform(post(ApiConstants.AUTH + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "phone", "+15551234567",
                                "password", "Password1"))))
                .andExpect(status().isCreated());
    }

    @Test
    void registerWithoutEmailAndPhoneReturnsBadRequest() throws Exception {
        mockMvc.perform(post(ApiConstants.AUTH + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("password", "Password1"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email or phone is required"));
    }

    @Test
    void registerWithInvalidEmailReturnsBadRequest() throws Exception {
        mockMvc.perform(post(ApiConstants.AUTH + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "not-an-email",
                                "password", "Password1"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerWithInvalidPhoneReturnsBadRequest() throws Exception {
        mockMvc.perform(post(ApiConstants.AUTH + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "phone", "123",
                                "password", "Password1"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerWithInvalidPasswordReturnsBadRequest() throws Exception {
        mockMvc.perform(post(ApiConstants.AUTH + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "weak@example.com",
                                "password", "short"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerDuplicateEmailReturnsConflict() throws Exception {
        registerEmail("dup@example.com");

        mockMvc.perform(post(ApiConstants.AUTH + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "dup@example.com",
                                "password", "Password1"))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already registered"));
    }

    @Test
    void registerDuplicatePhoneReturnsConflict() throws Exception {
        mockMvc.perform(post(ApiConstants.AUTH + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "phone", "+15559876543",
                                "password", "Password1"))))
                .andExpect(status().isCreated());

        mockMvc.perform(post(ApiConstants.AUTH + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "phone", "+15559876543",
                                "password", "Password1"))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Phone already registered"));
    }

    @Test
    void loginWithEmailSucceeds() throws Exception {
        registerEmail("login-email@example.com");

        mockMvc.perform(post(ApiConstants.AUTH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "login-email@example.com",
                                "password", "Password1"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.user.email").value("login-email@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void loginWithPhoneSucceeds() throws Exception {
        mockMvc.perform(post(ApiConstants.AUTH + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "phone", "+15551112222",
                                "password", "Password1"))))
                .andExpect(status().isCreated());

        mockMvc.perform(post(ApiConstants.AUTH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "phone", "+15551112222",
                                "password", "Password1"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void loginWithIncorrectPasswordReturnsUnauthorized() throws Exception {
        registerEmail("badpass@example.com");

        mockMvc.perform(post(ApiConstants.AUTH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "badpass@example.com",
                                "password", "WrongPass1"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    @Test
    void loginWithUnknownUserReturnsUnauthorized() throws Exception {
        mockMvc.perform(post(ApiConstants.AUTH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "nobody@example.com",
                                "password", "Password1"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    @Test
    void loginWithoutEmailAndPhoneReturnsBadRequest() throws Exception {
        mockMvc.perform(post(ApiConstants.AUTH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("password", "Password1"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void healthAndAuthEndpointsArePublic() throws Exception {
        mockMvc.perform(get(ApiConstants.HEALTH)).andExpect(status().isOk());
        mockMvc.perform(post(ApiConstants.AUTH + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "public@example.com",
                                "password", "Password1"))))
                .andExpect(status().isCreated());
    }

    @Test
    void protectedEndpointRejectsUnauthenticatedRequests() throws Exception {
        mockMvc.perform(get(ApiConstants.USERS + "/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Authentication required"));
    }

    @Test
    void authenticatedUserCanReadProfileWithoutPassword() throws Exception {
        String token = loginToken("profile@example.com");

        mockMvc.perform(get(ApiConstants.USERS + "/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("profile@example.com"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$", not(containsString("password"))));
    }

    @Test
    void invalidTokenIsRejected() throws Exception {
        mockMvc.perform(get(ApiConstants.USERS + "/me").header("Authorization", "Bearer not-a-valid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid or expired token"));
    }

    @Test
    void expiredTokenIsRejected() throws Exception {
        String validToken = loginToken("expired@example.com");
        String userId = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(
                        "test-secret-key-that-is-at-least-32-bytes-long".getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(validToken)
                .getPayload()
                .getSubject();
        Instant past = Instant.now().minusSeconds(3600);
        String expired = Jwts.builder()
                .subject(userId)
                .issuedAt(Date.from(past.minusSeconds(60)))
                .expiration(Date.from(past))
                .signWith(Keys.hmacShaKeyFor(
                        "test-secret-key-that-is-at-least-32-bytes-long".getBytes(StandardCharsets.UTF_8)))
                .compact();

        mockMvc.perform(get(ApiConstants.USERS + "/me").header("Authorization", "Bearer " + expired))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid or expired token"));
    }

    @Test
    void changePasswordSucceeds() throws Exception {
        String token = loginToken("changepw@example.com");

        mockMvc.perform(put(ApiConstants.USERS + "/password")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "currentPassword", "Password1",
                                "newPassword", "NewPass12"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password changed successfully"));

        mockMvc.perform(post(ApiConstants.AUTH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", "changepw@example.com",
                                "password", "Password1"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void changePasswordRejectsIncorrectCurrentPassword() throws Exception {
        String token = loginToken("wrongcurrent@example.com");

        mockMvc.perform(put(ApiConstants.USERS + "/password")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "currentPassword", "WrongPass1",
                                "newPassword", "NewPass12"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Current password is incorrect"));
    }

    @Test
    void changePasswordRejectsInvalidNewPassword() throws Exception {
        String token = loginToken("weaknew@example.com");

        mockMvc.perform(put(ApiConstants.USERS + "/password")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "currentPassword", "Password1",
                                "newPassword", "short"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void changePasswordRejectsUnauthenticatedRequest() throws Exception {
        mockMvc.perform(put(ApiConstants.USERS + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "currentPassword", "Password1",
                                "newPassword", "NewPass12"))))
                .andExpect(status().isUnauthorized());
    }

    private void registerEmail(String email) throws Exception {
        mockMvc.perform(post(ApiConstants.AUTH + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("email", email, "password", "Password1"))))
                .andExpect(status().isCreated());
    }

    private String loginToken(String email) throws Exception {
        registerEmail(email);
        MvcResult result = mockMvc.perform(post(ApiConstants.AUTH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("email", email, "password", "Password1"))))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
    }
}
