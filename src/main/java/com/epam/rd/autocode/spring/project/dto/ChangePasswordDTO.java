package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDTO {
    @NotBlank(message = "{validation.password.current_required}")
    private String currentPassword;

    @NotBlank(message = "{validation.password.new_required}")
    @Size(min = 8, message = "{validation.password.size}")
    @Pattern.List({
            @Pattern(regexp = ".*[0-9].*", message = "{validation.password.digit}"),
            @Pattern(regexp = ".*[a-z].*", message = "{validation.password.lowercase}"),
            @Pattern(regexp = ".*[A-Z].*", message = "{validation.password.uppercase}"),
    })
    private String newPassword;
}
