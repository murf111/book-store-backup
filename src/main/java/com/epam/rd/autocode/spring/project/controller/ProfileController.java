package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.ChangePasswordDTO;
import com.epam.rd.autocode.spring.project.dto.PaymentRequestDTO;
import com.epam.rd.autocode.spring.project.dto.UserDTO;
import com.epam.rd.autocode.spring.project.service.ClientService;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import com.epam.rd.autocode.spring.project.service.PaymentService;
import com.epam.rd.autocode.spring.project.service.UserService;
import com.epam.rd.autocode.spring.project.util.ViewNames;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

import static com.epam.rd.autocode.spring.project.util.Routes.PROFILE;

@Controller
@RequestMapping(PROFILE)
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final PaymentService paymentService;
    private final MessageSource messageSource;


    @GetMapping
    public String showProfile(Model model) {
        UserDTO currentUser = userService.getCurrentUser();
        String email = currentUser.getEmail();

        if ("CLIENT".equals(currentUser.getRole())) {
            model.addAttribute("user", clientService.getClientByEmail(email));
        } else {
            model.addAttribute("user", employeeService.getEmployeeByEmail(email));
        }

        if (!model.containsAttribute("changePasswordDTO")) {
            model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
        }

        if (!model.containsAttribute("paymentRequest")) {
            model.addAttribute("paymentRequest", new PaymentRequestDTO());
        }
        return ViewNames.VIEW_PROFILE;
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam String name,
                                @RequestParam(required = false) String phone,
                                @RequestParam(required = false) LocalDate birthDate) {

        String email = userService.getCurrentUser().getEmail();
        userService.updatePersonalData(email, name, phone, birthDate);

        return ViewNames.REDIRECT_PROFILE + "?success";
    }

    @PostMapping("/topup")
    public String topUpBalance(@ModelAttribute("paymentRequest") @Valid PaymentRequestDTO paymentRequest,
                               BindingResult bindingResult,
                               Authentication authentication,
                               Model model) {

        if (bindingResult.hasErrors()) {
            // RELOAD DATA so the page doesn't crash
            addCommonAttributes(model);

            // Add custom error message for the view to display
            String msg = messageSource.getMessage("profile.topup.error.general", null, LocaleContextHolder.getLocale());
            model.addAttribute("errorMessage", msg);

            // Return VIEW directly (Stateless)
            return ViewNames.VIEW_PROFILE;
        }

        paymentService.processPayment(authentication.getName(), paymentRequest);

        return ViewNames.REDIRECT_PROFILE;
    }

    // MAYBE SOME REFACTOR
    @PostMapping("/password")
    public String changePassword(@ModelAttribute("changePasswordDTO") @Valid ChangePasswordDTO changePasswordDTO,
                                 BindingResult bindingResult,
                                 Model model) {

        if (bindingResult.hasErrors()) {
            addCommonAttributes(model);
            return ViewNames.VIEW_PROFILE;
        }

        // Attempt to change password
        boolean isChanged = userService.changePassword(changePasswordDTO.getCurrentPassword(),
                                                       changePasswordDTO.getNewPassword());

        if (!isChanged) {
            bindingResult.rejectValue("currentPassword", "error.currentPassword", "{validation.password.invalid_current");
            addCommonAttributes(model);
            return ViewNames.VIEW_PROFILE;
        }

        return ViewNames.REDIRECT_PROFILE + "?successPassword";
    }

    @DeleteMapping
    public String deleteAccount() {
        userService.deleteCurrentUser();
        return ViewNames.REDIRECT_LOGOUT;
    }

    // --- HELPER METHOD TO LOAD COMMON DATA ---
    private void addCommonAttributes(Model model) {
        UserDTO currentUser = userService.getCurrentUser();
        String email = currentUser.getEmail();

        // 1. Load specific user type details
        if ("CLIENT".equals(currentUser.getRole())) {
            model.addAttribute("user", clientService.getClientByEmail(email));
        } else {
            model.addAttribute("user", employeeService.getEmployeeByEmail(email));
        }

        // 2. Ensure Forms exist if not already in Model (e.g. from validation errors)
        if (!model.containsAttribute("changePasswordDTO")) {
            model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
        }
        if (!model.containsAttribute("paymentRequest")) {
            model.addAttribute("paymentRequest", new PaymentRequestDTO());
        }
    }
}
