package com.epam.rd.autocode.spring.project.model;

import com.epam.rd.autocode.spring.project.model.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Represents an internal staff member with administrative privileges.
 */
@Entity
@Table(name = "EMPLOYEES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Employee extends User{

    @NotBlank
    @Column(name = "PHONE")
    private String phone;

    @NotNull
    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    @PrePersist
    public void prePersist() {
        if (this.role == null) {
            this.role = Role.EMPLOYEE;
        }
    }
}
