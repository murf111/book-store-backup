package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.util.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.epam.rd.autocode.spring.project.util.Routes.LOGIN;

@Controller
@RequestMapping(LOGIN)
public class LoginController {

    @GetMapping
    public String login() {
        return ViewNames.VIEW_LOGIN;
    }
}