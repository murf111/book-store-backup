package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Web MVC tests for {@link BookController}.
 * <p>
 * Verifies HTTP endpoints, request mapping, status codes, and model attributes.
 * </p>
 */
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private BookService bookService;

    /**
     * Tests the {@code GET /books} endpoint.
     * <p>
     * <strong>Scenario:</strong> A user requests the book catalog page.
     * </p>
     * <strong>Checks:</strong>
     * <ul>
     * <li>HTTP Status is 200 OK.</li>
     * <li>The view name is "books/list".</li>
     * <li>The model contains the "books" attribute (Page object).</li>
     * </ul>
     */
    @Test
    @WithMockUser
    void getBooks_ShouldReturnListView() throws Exception {
        Page<BookDTO> page = new PageImpl<>(Collections.emptyList());
        when(bookService.findBooks(any(), any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/books"))
               .andExpect(status().isOk())
               .andExpect(view().name("books/list"))
               .andExpect(model().attributeExists("books"));
    }

    /**
     * Tests the {@code POST /books} endpoint.
     * <p>
     * <strong>Scenario:</strong> An EMPLOYEE submits a valid form to create a new book.
     * </p>
     * <strong>Checks:</strong>
     * <ul>
     * <li>HTTP Status is 3xx Redirection.</li>
     * <li>The user is redirected to the "/books" page after successful creation.</li>
     * </ul>
     */
    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void addBook_ShouldRedirect_WhenValid() throws Exception {
        mockMvc.perform(post("/books")
                                .with(csrf())
                                .param("name", "New Book")
                                .param("genre", "Fantasy")
                                .param("price", "10.00")
                                .param("pages", "100")
                                .param("language", "ENGLISH")
                                .param("author", "Author")
                                .param("ageGroup", "ADULT")
                                .param("characteristics", "Hardcover")
                                .param("description", "A good book")
                                .param("publicationDate", "2023-01-01"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/books"));
    }
}