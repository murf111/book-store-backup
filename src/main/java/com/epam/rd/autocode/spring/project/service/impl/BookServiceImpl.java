package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.model.enums.AgeGroup;
import com.epam.rd.autocode.spring.project.model.enums.Language;
import com.epam.rd.autocode.spring.project.repository.BookRepository;
import com.epam.rd.autocode.spring.project.repository.specification.BookSpecification;
import com.epam.rd.autocode.spring.project.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
                             .stream()
                             .map(book -> modelMapper.map(book, BookDTO.class))
                             .toList();
    }

    @Override
    public BookDTO getBookByName(String name) {
        return bookRepository.findByName(name)
                             .map(book -> modelMapper.map(book, BookDTO.class))
                             .orElseThrow(() -> new NotFoundException
                                     ("Book with name " + name + " was not found"));
    }

    @Override
    public BookDTO getBookById(Long id) {
        return bookRepository.findById(id)
                             .map(book -> modelMapper.map(book, BookDTO.class))
                             .orElseThrow(() -> new NotFoundException
                                     ("Book with id " + id + " was not found"));
    }

    @Override
    public List<BookDTO> getBooksByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooks();
        }

        // ADD modelMapper
        List<Book> books = bookRepository
                .findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);

        return books.stream()
                    .map(book -> modelMapper.map(book, BookDTO.class))
                    .toList();
    }

    @Override
    public Page<BookDTO> findBooks(String keyword, String genre, BigDecimal minPrice, BigDecimal maxPrice,
                                   AgeGroup ageGroup, Language language, Pageable pageable) {

        Specification<Book> spec = Specification.where(BookSpecification.hasKeyword(keyword))
                                                .and(BookSpecification.hasGenre(genre))
                                                .and(BookSpecification.priceGreaterThan(minPrice))
                                                .and(BookSpecification.priceLessThan(maxPrice))
                                                .and(BookSpecification.hasAgeGroup(ageGroup))
                                                .and(BookSpecification.hasLanguage(language));

        Page<Book> bookPage = bookRepository.findAll(spec, pageable);

        return bookPage.map(book -> modelMapper.map(book, BookDTO.class));
    }

    @Override
    @Transactional
    public BookDTO updateBookByName(String name, BookDTO bookDTO) {
        Book existingBook = bookRepository.findByName(name)
                                          .orElseThrow(() -> new NotFoundException
                                                  ("Book with name " + name + " was not found"));

        modelMapper.map(bookDTO, existingBook);

        Book savedBook = bookRepository.save(existingBook);

        return modelMapper.map(savedBook, BookDTO.class);
    }

    @Override
    @Transactional
    public BookDTO updateBookById(Long id, BookDTO bookDTO) {
        Book existingBook = bookRepository.findById(id)
                                          .orElseThrow(() -> new NotFoundException
                                                  ("Book with name " + bookDTO.getName() + " was not found"));

        if (bookDTO.getImageUrl() == null && existingBook.getImageUrl() != null) {
            bookDTO.setImageUrl(existingBook.getImageUrl());
        }

        modelMapper.map(bookDTO, existingBook);

        Book savedBook = bookRepository.save(existingBook);
        return modelMapper.map(savedBook, BookDTO.class);
    }

    @Override
    @Transactional
    public void deleteBookByName(String name) {
        bookRepository.delete(bookRepository
                                      .findByName(name)
                                      .orElseThrow(() -> new NotFoundException
                                              ("Book with name " + name + " was not found")));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('EMPLOYEE')")
    public void deleteBookById(Long id) {
        bookRepository.delete(bookRepository
                                      .findById(id)
                                      .orElseThrow(() -> new NotFoundException
                                              ("Book with id " + id + " was not found")));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('EMPLOYEE')")
    public BookDTO addBook(BookDTO book) {
        if (bookRepository.findByName(book.getName()).isPresent()) {
            throw new AlreadyExistException("Book already exists with name: " + book.getName());
        }
        Book newBook = modelMapper.map(book, Book.class);

        Book savedBook = bookRepository.save(newBook);
        return modelMapper.map(savedBook, BookDTO.class);
    }
}
