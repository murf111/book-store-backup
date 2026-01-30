package com.epam.rd.autocode.spring.project.model;

import com.epam.rd.autocode.spring.project.model.enums.AgeGroup;
import com.epam.rd.autocode.spring.project.model.enums.Language;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "BOOKS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @NotBlank
    @Column(name = "GENRE", nullable = false)
    private String genre;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "AGE_GROUP", nullable = false)
    private AgeGroup ageGroup;

    @NotNull
    @Positive
    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @NotNull
    @Column(name = "PUBLICATION_YEAR", nullable = false)
    private LocalDate publicationDate;

    @NotBlank
    @Column(name = "AUTHOR", nullable = false)
    private String author;

    @NotNull
    @Positive
    @Column(name = "NUMBER_OF_PAGES", nullable = false)
    private Integer pages;

    @NotBlank
    @Column(name = "CHARACTERISTICS", nullable = false)
    private String characteristics;

    @NotBlank
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "LANGUAGE", nullable = false)
    private Language language;

    @Column(name = "IMAGE_URL")
    private String imageUrl;
}