package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public BookDTO updateBookByName(String name, BookDTO book) {
        Book existingBook = bookRepository.findByName(name)
                                          .orElseThrow(() -> new NotFoundException
                                                  ("Book with name " + name + " was not found"));

        modelMapper.map(book, existingBook);

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
    public BookDTO addBook(BookDTO book) {
        if (bookRepository.findByName(book.getName()).isPresent()) {
            throw new AlreadyExistException("Book already exists with name: " + book.getName());
        }
        Book newBook = modelMapper.map(book, Book.class);

        Book savedBook = bookRepository.save(newBook);
        return modelMapper.map(savedBook, BookDTO.class);
    }
}
