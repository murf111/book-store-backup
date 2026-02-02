package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.model.enums.AgeGroup;
import com.epam.rd.autocode.spring.project.model.enums.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface BookService {

    List<BookDTO> getAllBooks();

    BookDTO getBookById(Long id);

    BookDTO getBookByName(String name);

    List<BookDTO> getBooksByKeyword(String keyword);

    Page<BookDTO> findBooks(String keyword, String genre, BigDecimal minPrice, BigDecimal maxPrice,
                            AgeGroup ageGroup, Language language, Pageable pageable);

    BookDTO addBook(BookDTO book);

    BookDTO updateBookByName(String name, BookDTO book);

    BookDTO updateBookById(Long id, BookDTO book);

    void deleteBookByName(String name);

    void deleteBookById(Long id);
}
