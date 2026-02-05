package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.service.BookService;
import com.epam.rd.autocode.spring.project.util.ViewNames;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.epam.rd.autocode.spring.project.util.Routes.HOME;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class HomeController {

    private final BookService bookService;

    @GetMapping(HOME)
    public String home(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return ViewNames.VIEW_HOME;
    }
}
