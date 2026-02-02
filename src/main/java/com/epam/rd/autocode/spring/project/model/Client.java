package com.epam.rd.autocode.spring.project.model;

import com.epam.rd.autocode.spring.project.model.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * Represents a customer entity within the system.
 * <p>
 * Extends {@link User} to inherit authentication credentials.
 * Includes financial attributes and security status specific to customers.
 * </p>
 */
@Entity
@Table(name = "CLIENTS") // Linked to USERS table by ID
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Client extends User{

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "BALANCE", nullable = false)
    private BigDecimal balance;

    @Column(name = "IS_BLOCKED", nullable = false)
    @Builder.Default
    private Boolean isBlocked = false;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private ShoppingCart shoppingCart;

    // Before saving a new Client to DB, automatically set the Role to CLIENT
    @PrePersist
    public void prePersist() {
        if (this.role == null) {
            this.role = Role.CLIENT;
        }
    }
}
