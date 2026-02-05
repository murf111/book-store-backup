package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.FileStorageException;
import com.epam.rd.autocode.spring.project.model.enums.AgeGroup;
import com.epam.rd.autocode.spring.project.model.enums.Language;
import com.epam.rd.autocode.spring.project.service.BookService;
import com.epam.rd.autocode.spring.project.util.BookSortOption;
import com.epam.rd.autocode.spring.project.util.FileUploadUtil;
import com.epam.rd.autocode.spring.project.util.ViewNames;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

import static com.epam.rd.autocode.spring.project.util.Routes.BOOKS;

@Controller
@RequestMapping(BOOKS)
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

        // 1. Resolve Sorting
        Sort sortObj = BookSortOption.fromValue(sort).getSortValue();
        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortObj);

        // 2. Fetch Data
        Page<BookDTO> bookPage = bookService.findBooks(keyword, genre, minPrice, maxPrice, ageGroup, language, customPageable);

        // 3. Populate Model
        model.addAttribute("books", bookPage);
        model.addAttribute("keyword", keyword);
        // Thymeleaf helper variables
        model.addAttribute("currentPage", bookPage.getNumber());
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("totalItems", bookPage.getTotalElements());

        return ViewNames.VIEW_BOOKS_LIST;
    }

    @GetMapping("/{name}")
    public String getBookByName(@PathVariable String name, Model model) {
        model.addAttribute("book", bookService.getBookByName(name));
        return ViewNames.VIEW_BOOKS_DETAIL;
    }

    @GetMapping("/search/{keyword}")
    public String getBookByKeyword(@PathVariable String keyword, Model model) {

        if (keyword == null || keyword.trim().length() < 2) {
            // Redirect back to main list with an error or just empty
            return ViewNames.REDIRECT_BOOKS;
        }

        model.addAttribute("books", bookService.getBooksByKeyword(keyword));
        return ViewNames.VIEW_BOOKS_LIST;
    }

    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new BookDTO());
        return ViewNames.VIEW_BOOKS_ADD;
    }

    @PostMapping
    public String addBook(@ModelAttribute("book") @Valid BookDTO bookDTO,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            return ViewNames.VIEW_BOOKS_ADD;
        }

        try {
            processImageUpload(bookDTO);
            bookService.addBook(bookDTO);
        } catch (AlreadyExistException e) {
            bindingResult.rejectValue("name", "error.book", e.getMessage());
            return ViewNames.VIEW_BOOKS_ADD;
        } catch (FileStorageException e) {
            // Handle File Upload Error
            bindingResult.reject("error.upload", "Failed to upload image: " + e.getMessage());
            return ViewNames.VIEW_BOOKS_ADD;
        }

        return ViewNames.REDIRECT_BOOKS;
    }

    @GetMapping("/{name}/edit")
    public String showEditBookForm(@PathVariable String name, Model model) {
        BookDTO bookDTO = bookService.getBookByName(name);
        model.addAttribute("book", bookDTO);
        return ViewNames.VIEW_BOOKS_EDIT;
    }

    @PatchMapping("/{id}")
    public String updateBookById(@PathVariable Long id, @ModelAttribute("book") @Valid BookDTO bookDTO,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewNames.VIEW_BOOKS_EDIT;
        }

        try {
            processImageUpload(bookDTO);

            bookService.updateBookById(id, bookDTO);

        } catch (FileStorageException e) {
            bindingResult.reject("error.upload", "Failed to upload image: " + e.getMessage());
            return ViewNames.VIEW_BOOKS_EDIT;
        }

        return ViewNames.REDIRECT_BOOKS + "/" + bookDTO.getName();
    }

    @DeleteMapping("/{id}")
    public String deleteBookById(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookService.deleteBookById(id);

        redirectAttributes.addFlashAttribute("successMessage", "Book deleted successfully!");

        return ViewNames.REDIRECT_BOOKS;
    }

    private void processImageUpload(BookDTO bookDTO) {
        if (bookDTO.getImageFile() != null && !bookDTO.getImageFile().isEmpty()) {
            String fileName = FileUploadUtil.saveFile(bookDTO.getImageFile());
            bookDTO.setImageUrl(fileName);
        }
    }
}
