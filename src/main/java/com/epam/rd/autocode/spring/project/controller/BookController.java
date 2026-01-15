package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{name}")
    public BookDTO getBookByName(@PathVariable String name) {
        return bookService.getBookByName(name);
    }

    @PostMapping
    public ResponseEntity<BookDTO> addBook(@RequestBody @Valid BookDTO bookDTO) {
        BookDTO createdBook = bookService.addBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @PatchMapping("/{name}")
    public BookDTO updateBookByName(@PathVariable String name, @RequestBody @Valid BookDTO book) {
        return bookService.updateBookByName(name, book);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteBookByName(@PathVariable String name) {
        bookService.deleteBookByName(name);
        return ResponseEntity.noContent().build();
    }
}
