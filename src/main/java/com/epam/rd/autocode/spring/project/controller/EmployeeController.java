package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import java.util.List;

@Controller
@RequestMapping("/staff")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

//    @GetMapping
//    public String getAllEmployees(Model model) {
//        model.addAttribute("staff", employeeService.getAllEmployees());
//        return "staff";
//    }

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public String showStaffDirectory(Model model) {
        // Load the list of employees
        model.addAttribute("employees", employeeService.getAllEmployees());

        // Add an empty object for the "Add Employee" form to bind to
        if (!model.containsAttribute("newEmployee")) {
            model.addAttribute("newEmployee", new EmployeeDTO());
        }

        return "staff"; // Maps to src/main/resources/templates/staff/employee-list.html
    }

    @GetMapping("/{email}")
    public EmployeeDTO getEmployeeByEmail(@PathVariable String email) {
        return employeeService.getEmployeeByEmail(email);
    }

//    @PostMapping
//    public ResponseEntity<EmployeeDTO> addEmployee(@RequestParam @Valid EmployeeDTO employeeDTO) {
//        EmployeeDTO employee = employeeService.addEmployee(employeeDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
//    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String addEmployee(@Valid @ModelAttribute("newEmployee") EmployeeDTO employeeDTO,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes,
                              Model model) {

        // If validation fails (e.g., blank name), reload page with errors
        if (bindingResult.hasErrors()) {
            // Reload the list so the table doesn't disappear
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "staff";
        }

        try {
            employeeService.addEmployee(employeeDTO);
            redirectAttributes.addFlashAttribute("successMessage", "staff.notify.success");
            return "redirect:/staff"; // Post-Redirect-Get pattern
        } catch (Exception e) {
            // If service throws error (e.g. Email exists), show it
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "staff";
        }
    }

    @PatchMapping("/{email}")
    public EmployeeDTO updateEmployeeByEmail(@PathVariable String email, @RequestParam @Valid EmployeeDTO employeeDTO) {
        return employeeService.updateEmployeeByEmail(email, employeeDTO);
    }

    @DeleteMapping("/{email}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String deleteEmployeeByEmail(@PathVariable String email) {
        employeeService.deleteEmployeeByEmail(email);
        return "redirect:/staff";
    }
}
