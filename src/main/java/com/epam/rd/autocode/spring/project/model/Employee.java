package com.epam.rd.autocode.spring.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "EMPLOYEES")
@Data
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Employee extends User{
    @Column(name = "PHONE")
    private String phone;

    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    public Employee(Long id, String email, String name, String password, String phone, LocalDate birthDate) {
        super(id, email, name, password);
        this.phone = phone;
        this.birthDate = birthDate;
    }
}
