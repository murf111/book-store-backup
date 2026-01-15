package com.epam.rd.autocode.spring.project.dto;

import com.epam.rd.autocode.spring.project.model.enums.AgeGroup;
import com.epam.rd.autocode.spring.project.model.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO{
    @NotBlank
    private String name;

    @NotBlank
    private String genre;

    @NotNull
    private AgeGroup ageGroup;

    @NotNull
    private BigDecimal price;

    @NotNull
    private LocalDate publicationDate;

    @NotBlank
    private String author;

    @NotNull
    @Positive
    private Integer pages;

    @NotBlank
    private String characteristics;

    @NotBlank
    private String description;

    @NotNull
    private Language language;
}
