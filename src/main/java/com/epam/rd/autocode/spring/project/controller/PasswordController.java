package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.PasswordResetDTO;
import com.epam.rd.autocode.spring.project.exception.InvalidTokenException;
import com.epam.rd.autocode.spring.project.service.PasswordRecoveryService;
import com.epam.rd.autocode.spring.project.util.ViewNames;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

import static com.epam.rd.autocode.spring.project.util.Routes.PASSWORD;
import static com.epam.rd.autocode.spring.project.util.Routes.PASSWORD_FORGOT;

@Controller
@RequestMapping(PASSWORD)
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordRecoveryService recoveryService;
    private final MessageSource messageSource;

    @GetMapping("/forgot")
    public String showForgotForm() {
        return ViewNames.VIEW_PASSWORD_FORGOT;
    }

    @PostMapping("/forgot")
    public String processForgot(@RequestParam String email,
                                RedirectAttributes redirectAttributes,
                                Locale locale) {
        recoveryService.processForgotPassword(email);

        String successMsg = messageSource.getMessage("password.forgot.success", null, locale);
        redirectAttributes.addFlashAttribute("message", successMsg);
        return ViewNames.REDIRECT_PASSWORD_FORGOT;
    }

    @GetMapping("/reset")
    public String showResetForm(@RequestParam String email, @RequestParam String token, Model model) {
        PasswordResetDTO passwordResetDTO = new PasswordResetDTO();
        passwordResetDTO.setEmail(email);
        passwordResetDTO.setToken(token);
        model.addAttribute("passwordResetDTO", passwordResetDTO);
        return ViewNames.VIEW_PASSWORD_RESET;
    }

    @PostMapping("/reset")
    public String processReset(@ModelAttribute("passwordResetDTO") @Valid PasswordResetDTO dto,
                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ViewNames.VIEW_PASSWORD_RESET;
        }

        try {
            recoveryService.resetPassword(dto.getEmail(), dto.getToken(), dto.getNewPassword());
            return ViewNames.REDIRECT_LOGIN + "?resetSuccess";
        } catch (InvalidTokenException e) {
            return "redirect:" + PASSWORD_FORGOT + "?error=" + e.getMessage();
        }
    }
}
