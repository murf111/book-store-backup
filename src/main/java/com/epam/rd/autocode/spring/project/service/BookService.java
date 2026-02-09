package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.model.enums.AgeGroup;
import com.epam.rd.autocode.spring.project.model.enums.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;


/**
 * Service interface for managing books in the book store.
 *
 * <p>Provides operations for:</p>
 * <ul>
 *   <li>CRUD operations on books</li>
 *   <li>Searching and filtering books</li>
 *   <li>Pagination and sorting</li>
 * </ul>
 *
 * <p>All operations return DTOs to maintain separation between
 * domain models and API responses.</p>
 *
 * @author Denys Sych
 * @version 1.0
 * @since 2026
 * @see BookDTO
 * @see com.epam.rd.autocode.spring.project.model.Book
 */
public interface BookService {

    List<BookDTO> getAllBooks();

    /**
     * Retrieves a single book by its ID.
     *
     * @param id the book ID
     * @return book DTO
     * @throws com.epam.rd.autocode.spring.project.exception.NotFoundException
     *         if book not found
     */
    BookDTO getBookById(Long id);

    BookDTO getBookByName(String name);

    List<BookDTO> getBooksByKeyword(String keyword);

    /**
     * Retrieves a paginated list of books with optional filtering.
     *
     * @param pageable pagination parameters (page, size, sort)
     * @param language filter by language (null for all languages)
     * @param ageGroup filter by age group (null for all ages)
     * @param keyword search term for title or author (null for no search)
     * @return page of books matching the criteria
     * @throws IllegalArgumentException if pageable is null
     */
    Page<BookDTO> findBooks(String keyword, String genre, BigDecimal minPrice, BigDecimal maxPrice,
                            AgeGroup ageGroup, Language language, Pageable pageable);

    /**
     * Creates a new book in the system.
     *
     * <p>Requires EMPLOYEE role.</p>
     *
     * @param bookDTO the book data
     * @return created book with generated ID
     * @throws com.epam.rd.autocode.spring.project.exception.AlreadyExistException
     *         if book with same name already exists
     * @throws IllegalArgumentException if bookDTO is null or invalid
     */
    BookDTO addBook(BookDTO bookDTO);

    BookDTO updateBookByName(String name, BookDTO book);

    /**
     * Updates an existing book.
     *
     * <p>Requires EMPLOYEE role.</p>
     *
     * @param id the book ID to update
     * @param bookDTO the updated book data
     * @return updated book DTO
     * @throws com.epam.rd.autocode.spring.project.exception.NotFoundException
     *         if book not found
     */
    BookDTO updateBookById(Long id, BookDTO bookDTO);

    void deleteBookByName(String name);

    /**
     * Deletes a book from the system.
     *
     * <p>Requires EMPLOYEE role.</p>
     * <p>Note: This operation cannot be undone.</p>
     *
     * @param id the book ID to delete
     * @throws com.epam.rd.autocode.spring.project.exception.NotFoundException
     *         if book not found
     */
    void deleteBookById(Long id);
}
