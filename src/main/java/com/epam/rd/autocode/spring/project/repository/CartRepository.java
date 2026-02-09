package com.epam.rd.autocode.spring.project.repository;

import com.epam.rd.autocode.spring.project.model.ShoppingCart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<ShoppingCart, Long> {

    @EntityGraph(attributePaths = {"items", "items.book"})
    Optional<ShoppingCart> findByClientEmail(String email);
}
