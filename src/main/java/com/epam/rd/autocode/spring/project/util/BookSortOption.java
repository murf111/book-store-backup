package com.epam.rd.autocode.spring.project.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum BookSortOption {

    NEWEST("newest", Sort.by(Sort.Direction.DESC, "id")),
    PRICE_ASC("price_asc", Sort.by(Sort.Direction.ASC, "price")),
    PRICE_DESC("price_desc", Sort.by(Sort.Direction.DESC, "price")),
    NAME_ASC("name_asc", Sort.by(Sort.Direction.ASC, "name"));

    private final String value;
    private final Sort sortValue;

    public static BookSortOption fromValue(String value) {
        return Arrays.stream(values())
                     .filter(opt -> opt.value.equalsIgnoreCase(value))
                     .findFirst()
                     .orElse(NEWEST); // Safe fallback
    }
}