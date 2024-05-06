package com.CMSC.Library.controller;

import com.CMSC.Library.domain.Book;
import com.CMSC.Library.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @Test
    public void testGetAllBooks() throws Exception {
        Book book = new Book("Author", true, false, "1234567890", "Title", null);
        when(bookRepository.findAll()).thenReturn(Collections.singletonList(book));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].author").value("Author"))
                .andExpect(jsonPath("$[0].isAudioBook").value(true))
                .andExpect(jsonPath("$[0].isCheckedOut").value(false))
                .andExpect(jsonPath("$[0].isbn").value("1234567890"))
                .andExpect(jsonPath("$[0].title").value("Title"));
    }

    @Test
    public void testGetBookById() throws Exception {
        Book book = new Book("Author", true, false, "1234567890", "Title", null);
        when(bookRepository.findById(1L)).thenReturn(java.util.Optional.of(book));

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.author").value("Author"))
                .andExpect(jsonPath("$.isAudioBook").value(true))
                .andExpect(jsonPath("$.isCheckedOut").value(false))
                .andExpect(jsonPath("$.isbn").value("1234567890"))
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    public void testCreateBook() throws Exception {
        Book book = new Book("Author", true, false, "1234567890", "Title", null);
        when(bookRepository.save(any())).thenReturn(book);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"author\":\"Author\",\"isAudioBook\":true,\"isCheckedOut\":false,\"isbn\":\"1234567890\",\"title\":\"Title\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.author").value("Author"))
                .andExpect(jsonPath("$.isAudioBook").value(true))
                .andExpect(jsonPath("$.isCheckedOut").value(false))
                .andExpect(jsonPath("$.isbn").value("1234567890"))
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    public void testGetAvailableBooks() throws Exception {
        Book availableBook = new Book("Author", true, false, "1234567890", "Title", null);
        Book checkedOutBook = new Book("Author2", true, true, "0987654321", "Title2", 123L);
        when(bookRepository.findAll()).thenReturn(Arrays.asList(availableBook, checkedOutBook));

        mockMvc.perform(get("/books/available"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].author").value("Author"))
                .andExpect(jsonPath("$[0].isAudioBook").value(true))
                .andExpect(jsonPath("$[0].isCheckedOut").value(false))
                .andExpect(jsonPath("$[0].isbn").value("1234567890"))
                .andExpect(jsonPath("$[0].title").value("Title"))
                .andExpect(jsonPath("$", hasSize(1))); // Only 1 available book
    }

    @Test
    public void testGetNumberOfBooks() throws Exception {
        when(bookRepository.count()).thenReturn(5L);

        mockMvc.perform(get("/books/numberOfBooks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(5));
    }

    @Test
    public void testGetNumberOfCheckedOutBooks() throws Exception {
        Book availableBook = new Book("Author", true, false, "1234567890", "Title", null);
        Book checkedOutBook = new Book("Author2", true, true, "0987654321", "Title2", 123L);
        when(bookRepository.findAll()).thenReturn(Arrays.asList(availableBook, checkedOutBook));

        mockMvc.perform(get("/books/numberOfCheckedOut"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(1)); // Only 1 book checked out
    }

    @Test
    public void testReplaceBook() throws Exception {
        Book existingBook = new Book("Old Author", true, false, "1234567890", "Old Title", null);
        Book updatedBook = new Book("New Author", false, true, "0987654321", "New Title", 123L);
        when(bookRepository.findById(1L)).thenReturn(java.util.Optional.of(existingBook));
        when(bookRepository.save(any())).thenReturn(updatedBook);

        mockMvc.perform(put("/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"author\":\"New Author\",\"isAudioBook\":false,\"isCheckedOut\":true,\"isbn\":\"0987654321\",\"title\":\"New Title\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.author").value("New Author"))
                .andExpect(jsonPath("$.isAudioBook").value(false))
                .andExpect(jsonPath("$.isCheckedOut").value(true))
                .andExpect(jsonPath("$.isbn").value("0987654321"))
                .andExpect(jsonPath("$.title").value("New Title"));
    }

    @Test
    public void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());
    }
}
