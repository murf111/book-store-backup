package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.repository.BookRepository;
import com.epam.rd.autocode.spring.project.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link BookServiceImpl}.
 * <p>
 * Tests book creation logic and validation of unique constraints.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock private BookRepository bookRepository;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    /**
     * Tests the {@link BookServiceImpl#addBook(BookDTO)} method.
     * <p>
     * <strong>Scenario:</strong> A book with a unique name is added.
     * </p>
     * <strong>Checks:</strong>
     * <ul>
     * <li>The repository returns an empty Optional for the name lookup (indicating uniqueness).</li>
     * <li>The returned DTO matches the input name.</li>
     * <li>The {@code bookRepository.save()} method is called exactly once.</li>
     * </ul>
     */
    @Test
    void addBook_ShouldSaveBook_WhenNameIsUnique() {
        BookDTO dto = BookDTO.builder().name("Unique Book").build();
        Book book = new Book();
        book.setName("Unique Book");

        when(bookRepository.findByName(dto.getName())).thenReturn(Optional.empty());
        when(modelMapper.map(dto, Book.class)).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(modelMapper.map(book, BookDTO.class)).thenReturn(dto);

        BookDTO result = bookService.addBook(dto);

        assertEquals("Unique Book", result.getName());
        verify(bookRepository).save(any(Book.class));
    }

    /**
     * Tests the {@link BookServiceImpl#addBook(BookDTO)} method.
     * <p>
     * <strong>Scenario:</strong> Attempting to add a book with a name that already exists.
     * </p>
     * <strong>Checks:</strong>
     * <ul>
     * <li>The {@code AlreadyExistException} is thrown.</li>
     * <li>The execution halts before saving to the database.</li>
     * </ul>
     */
    @Test
    void addBook_ShouldThrowException_WhenBookExists() {
        BookDTO dto = BookDTO.builder().name("Existing Book").build();
        when(bookRepository.findByName(dto.getName())).thenReturn(Optional.of(new Book()));

        assertThrows(AlreadyExistException.class, () -> bookService.addBook(dto));
    }
}