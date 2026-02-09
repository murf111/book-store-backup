package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationDTO {
    @NotBlank(message = "{validation.user.name_required}")
    private String name;

    @NotBlank(message = "{validation.user.email_required}")
    @Email(message = "{validation.user.email_invalid}")
    private String email;

    @NotBlank(message = "{validation.password.required}")
    @Size(min = 8, message = "{validation.password.size}")
    @Pattern.List({
            @Pattern(regexp = ".*[0-9].*", message = "{validation.password.digit}"),
            @Pattern(regexp = ".*[a-z].*", message = "{validation.password.lowercase}"),
            @Pattern(regexp = ".*[A-Z].*", message = "{validation.password.uppercase}")
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
