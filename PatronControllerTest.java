package com.CMSC.Library.controller;

import com.CMSC.Library.domain.Patron;
import com.CMSC.Library.repositories.PatronRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PatronControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatronRepository patronRepository;

    @Test
    public void testGetAllPatrons() throws Exception {
        Patron patron = new Patron("John", "Doe", "john@example.com", "password", "1990-01-01", "Address");
        when(patronRepository.findAll()).thenReturn(Collections.singletonList(patron));

        mockMvc.perform(get("/patron"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"))
                .andExpect(jsonPath("$[0].password").doesNotExist()) // Password should not be exposed
                .andExpect(jsonPath("$[0].dateOfBirth").value("1990-01-01"))
                .andExpect(jsonPath("$[0].address").value("Address"));
    }

    @Test
    public void testGetPatronById() throws Exception {
        Patron patron = new Patron("John", "Doe", "john@example.com", "password", "1990-01-01", "Address");
        when(patronRepository.findById(1L)).thenReturn(java.util.Optional.of(patron));

        mockMvc.perform(get("/patron/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist()) // Password should not be exposed
                .andExpect(jsonPath("$.dateOfBirth").value("1990-01-01"))
                .andExpect(jsonPath("$.address").value("Address"));
    }

    @Test
    public void testCreatePatron() throws Exception {
        Patron patron = new Patron("John", "Doe", "john@example.com", "password", "1990-01-01", "Address");
        when(patronRepository.save(any())).thenReturn(patron);

        mockMvc.perform(post("/patron")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john@example.com\",\"password\":\"password\",\"dateOfBirth\":\"1990-01-01\",\"address\":\"Address\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist()) // Password should not be exposed
                .andExpect(jsonPath("$.dateOfBirth").value("1990-01-01"))
                .andExpect(jsonPath("$.address").value("Address"));
    }

    
}
