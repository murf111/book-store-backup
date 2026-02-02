package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.model.enums.AgeGroup;
import com.epam.rd.autocode.spring.project.model.enums.Language;
import com.epam.rd.autocode.spring.project.service.BookService;
import com.epam.rd.autocode.spring.project.util.FileUploadUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public String getBooks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) AgeGroup ageGroup,
            @RequestParam(required = false) Language language,
            @RequestParam(required = false, defaultValue = "newest") String sort,
            @PageableDefault(size = 8) Pageable pageable,
            Model model) {

        // Translating string "sort" param to actual Sort object
        Sort sortObj = Sort.unsorted();
        if ("price_asc".equals(sort)) {
            sortObj = Sort.by(Sort.Direction.ASC, "price");
        } else if ("price_desc".equals(sort)) {
            sortObj = Sort.by(Sort.Direction.DESC, "price");
        } else if ("name_asc".equals(sort)) {
            sortObj = Sort.by(Sort.Direction.ASC, "name");
        } else {
            // Default: Newest first (Sort by ID Descending)
            sortObj = Sort.by(Sort.Direction.DESC, "id");
        }

        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortObj);

        Page<BookDTO> bookPage = bookService.findBooks(keyword, genre, minPrice, maxPrice, ageGroup, language, customPageable);

        model.addAttribute("books", bookPage);
        // Thymeleaf helper variables
        model.addAttribute("currentPage", bookPage.getNumber());
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("totalItems", bookPage.getTotalElements());

        return "books/list";
    }

    @GetMapping("/{name}")
    public String getBookByName(@PathVariable String name, Model model) {
        model.addAttribute("book", bookService.getBookByName(name));
        return "books/detail";
    }

    @GetMapping("/search/{keyword}")
    public String getBookByKeyword(@PathVariable String keyword, Model model) {
        model.addAttribute("books", bookService.getBooksByKeyword(keyword));
        return "books/list";
    }

    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new BookDTO());
        return "books/add";
    }

    @PostMapping
    public String addBook(@ModelAttribute("book") @Valid BookDTO bookDTO,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            return "books/add";
        }

        try {
            if (bookDTO.getImageFile() != null && !bookDTO.getImageFile().isEmpty()) {
                String fileName = FileUploadUtil.saveFile(bookDTO.getImageFile());
                bookDTO.setImageUrl(fileName);
            }

            bookService.addBook(bookDTO);
        } catch (AlreadyExistException e) {
            bindingResult.rejectValue("name", "error.book", e.getMessage());
            return "books/add";
        } catch (IOException e) {
            // Handle File Upload Error
            e.printStackTrace(); // Good for debugging
            bindingResult.reject("error.upload", "Failed to upload image. Please try again.");
            return "books/add";
        }

        return "redirect:/books";
    }

    @GetMapping("/{name}/edit")
    public String showEditBookForm(@PathVariable String name, Model model) {
        BookDTO bookDTO = bookService.getBookByName(name);
        model.addAttribute("book", bookDTO);
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String updateBookById(@PathVariable Long id, @ModelAttribute("book") @Valid BookDTO bookDTO,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        try {
            // Handle Image Upload (Logic copied from addBook)
            if (bookDTO.getImageFile() != null && !bookDTO.getImageFile().isEmpty()) {
                String fileName = FileUploadUtil.saveFile(bookDTO.getImageFile());
                bookDTO.setImageUrl(fileName);
            }

            bookService.updateBookById(id, bookDTO);

        } catch (IOException e) {
            e.printStackTrace();
            bindingResult.reject("error.upload", "Failed to upload image.");
            return "books/edit";
        }

        return "redirect:/books/" + bookDTO.getName();
    }

    @DeleteMapping("/{id}")
    public String deleteBookById(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookService.deleteBookById(id);

        redirectAttributes.addFlashAttribute("successMessage", "Book deleted successfully!");

        return "redirect:/books";
    }
}
