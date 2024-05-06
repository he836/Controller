package com.CMSC.Library.controller;

import com.CMSC.Library.dto.SigninRequest;
import com.CMSC.Library.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    public void testSignIn() throws Exception {
        // Arrange
        SigninRequest request = new SigninRequest("username", "password");
        when(authService.signIn(request)).thenReturn(ResponseEntity.status(HttpStatus.OK).body("Some result"));

        // Act
        MvcResult mvcResult = mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"username\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        assertEquals("Some result", mvcResult.getResponse().getContentAsString());
    }
}
