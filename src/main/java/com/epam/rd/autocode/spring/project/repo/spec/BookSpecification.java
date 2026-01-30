package com.epam.rd.autocode.spring.project.repo.spec;

import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.model.enums.AgeGroup;
import com.epam.rd.autocode.spring.project.model.enums.Language;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class BookSpecification {

    // Rule: Filter by Keyword (checks Name OR Author)
    public static Specification<Book> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return null; // Ignore this rule
            }
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), likePattern)
            );
        };
    }

    // Rule: Filter by Genre
    public static Specification<Book> hasGenre(String genre) {
        return (root, query, criteriaBuilder) -> {
            if (genre == null || genre.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("genre"), genre);
        };
    }

    public static Specification<Book> hasAgeGroup(AgeGroup ageGroup) {
        return (root, query, criteriaBuilder) -> {
            if (ageGroup == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("ageGroup"), ageGroup);
        };
    }

    public static Specification<Book> hasLanguage(Language language) {
        return (root, query, criteriaBuilder) -> {
            if (language == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("language"), language);
        };
    }

    // Rule: Price >= Min
    public static Specification<Book> priceGreaterThan(BigDecimal minPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null) {
                return null;
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
        };
    }

    // Rule: Price <= Max
    public static Specification<Book> priceLessThan(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (maxPrice == null) {
                return null;
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }
}