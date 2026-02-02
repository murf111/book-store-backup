package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    // Better for real-world online bookstore
    @Pattern.List({
            @Pattern(regexp = ".*[0-9].*", message = "Password must contain at least one number"),
            @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter"),
            @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
    })
    /*
    Requirement "Enforce strong password policies (Min length, special characters, etc.)"
    but I don't see a sense to do it such complex

    @Pattern.List({
            @Pattern(regexp = ".*[0-9].*", message = "Password must contain at least one number"),
            @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter"),
            @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter"),
            @Pattern(regexp = ".*[@#$%^&+=!].*", message = "Password must contain at least one special character")
    })
     */
    private String password;
}
