package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.ChangePasswordDTO;
import com.epam.rd.autocode.spring.project.dto.UserDTO;
import com.epam.rd.autocode.spring.project.service.ClientService;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import com.epam.rd.autocode.spring.project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final ClientService clientService;
    private final EmployeeService employeeService;


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
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam String name,
                                @RequestParam(required = false) BigDecimal balance,
                                @RequestParam(required = false) String phone,
                                @RequestParam(required = false) LocalDate birthDate) {

        // 2. Get current user email safely
        String email = userService.getCurrentUser().getEmail();

        // 3. Call the smart service
        userService.updatePersonalData(email, name, balance, phone, birthDate);

        return "redirect:/profile?success";
    }

    @PostMapping("/password")
    public String changePassword(@ModelAttribute("changePasswordDTO") @Valid ChangePasswordDTO changePasswordDTO,
                                 BindingResult bindingResult,
                                 Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userService.getCurrentUser());
            return "profile";
        }

        // Attempt to change password
        boolean isChanged = userService.changePassword(changePasswordDTO.getCurrentPassword(),
                                                       changePasswordDTO.getNewPassword());

        if (!isChanged) {
            model.addAttribute("user", userService.getCurrentUser());
            // a global error
            bindingResult.rejectValue("currentPassword", "error.currentPassword", "Incorrect current password");
            return "profile";
        }

        return "redirect:/profile?successPassword";
    }

    @DeleteMapping
    public String deleteAccount() {
        userService.deleteCurrentUser();
        return "redirect:/logout";
    }
}
