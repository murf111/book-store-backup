package com.epam.rd.autocode.spring.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "CLIENTS")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Client extends User{

    @Column(name = "BALANCE")
    private BigDecimal balance;

    public Client(Long id, String email, String name, String password, BigDecimal balance) {
        super(id, email, name, password);
        this.balance = balance;
    }
}
