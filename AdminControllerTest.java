package com.CMSC.Library.controller;

import com.CMSC.Library.assembler.AdminModelAssembler;
import com.CMSC.Library.domain.Admin;
import com.CMSC.Library.repositories.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AdminControllerTest {

    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private AdminModelAssembler adminModelAssembler;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllAdmins() {
        // Arrange
        Admin admin = new Admin("John", "Doe", "john.doe@example.com", "password", "1990-01-01", "1234567890", "123 Main St", "admin");
        when(adminRepository.findAll()).thenReturn(Arrays.asList(admin));

        // Act
        CollectionModel<EntityModel<Admin>> result = adminController.all();

        // Assert
        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals(1, result.getContent().size());
    }

    @Test
    public void testGetAdminById() {
        // Arrange
        Admin admin = new Admin("John", "Doe", "john.doe@example.com", "password", "1990-01-01", "1234567890", "123 Main St", "admin");
        Long id = 1L;
        when(adminRepository.findById(id)).thenReturn(Optional.of(admin));

        // Act
        EntityModel<Admin> result = adminController.one(id);

        // Assert
        assertNotNull(result);
        assertEquals(admin.getFirstName(), result.getContent().getFirstName());
    }

    @Test
    public void testCreateAdmin() {
        // Arrange
        Admin admin = new Admin("John", "Doe", "john.doe@example.com", "password", "1990-01-01", "1234567890", "123 Main St", "admin");
        when(adminRepository.save(admin)).thenReturn(admin);

        // Act
        ResponseEntity<?> responseEntity = adminController.newAdmin(admin);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(201, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testUpdateAdmin() {
        // Arrange
        Long id = 1L;
        Admin existingAdmin = new Admin("John", "Doe", "john.doe@example.com", "password", "1990-01-01", "1234567890", "123 Main St", "admin");
        Admin updatedAdmin = new Admin("Jane", "Smith", "jane.smith@example.com", "newpassword", "1995-05-05", "9876543210", "456 Oak St", "superadmin");
        when(adminRepository.findById(id)).thenReturn(Optional.of(existingAdmin));
        when(adminRepository.save(existingAdmin)).thenReturn(updatedAdmin);

        // Act
        ResponseEntity<?> responseEntity = adminController.replaceAdmins(updatedAdmin, id);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(201, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testDeleteAdmin() {
        // Arrange
        Long id = 1L;

        // Act
        ResponseEntity<?> responseEntity = adminController.deleteAdmin(id);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(204, responseEntity.getStatusCodeValue());
    }
}

