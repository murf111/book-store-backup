package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;

import java.util.List;

/**
 * Service interface for managing Employee accounts.
 *
 * <p>Operations restricted to Employee role.</p>
 *
 * @author Denys Sych
 * @version 1.0
 * @since 2026
 * @see EmployeeDTO
 */
public interface EmployeeService {

    List<EmployeeDTO> getAllEmployees();

    /**
     * Retrieves a specific employee by email.
     *
     * @param email the employee's email
     * @return the employee DTO
     * @throws com.epam.rd.autocode.spring.project.exception.NotFoundException
     * if the employee is not found
     */
    EmployeeDTO getEmployeeByEmail(String email);

    /**
     * Updates an employee's information.
     *
     * @param email the email identifying the employee
     * @param employee the updated employee data
     * @return the updated employee DTO
     */
    EmployeeDTO updateEmployeeByEmail(String email, EmployeeDTO employee);

    /**
     * Deletes an employee account.
     *
     * @param email the email of the employee to delete
     */
    void deleteEmployeeByEmail(String email);

    /**
     * Creates a new employee account.
     *
     * @param employee the employee data
     * @return the created employee DTO
     * @throws com.epam.rd.autocode.spring.project.exception.AlreadyExistException
     * if a user with the same email exists
     */
    EmployeeDTO addEmployee(EmployeeDTO employee);
}
