package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO{
    @NotBlank
    @Email
    private String email;

    @NotBlank(message = "{validation.password.required}")
    @ToString.Exclude
    @Size(min = 8, message = "{validation.password.size}")
    @Pattern.List({
            @Pattern(regexp = ".*[0-9].*", message = "{validation.password.digit}"),
            @Pattern(regexp = ".*[a-z].*", message = "{validation.password.lowercase}"),
            @Pattern(regexp = ".*[A-Z].*", message = "{validation.password.uppercase}"),
    })
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotNull(message = "{validation.date.required}")
    @Past(message = "{validation.employee.birth_date_past}")
    private LocalDate birthDate;
}
