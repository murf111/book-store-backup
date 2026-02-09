package com.epam.rd.autocode.spring.project.repository;

import com.epam.rd.autocode.spring.project.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // SOLVED: Fetches orders + bookItems in one query, client and employee n+1 also solved
    @Override
    @EntityGraph(attributePaths = {"bookItems", "bookItems.book", "client", "employee"})
    List<Order> findAll();

    @EntityGraph(attributePaths = {"bookItems", "bookItems.book", "client", "employee"})
    List<Order> findAllByClientEmail(String email);

    @EntityGraph(attributePaths = {"bookItems", "bookItems.book", "client", "employee"})
    List<Order> findAllByEmployeeEmail(String email);
}
