package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.repository.EmployeeRepository;
import com.epam.rd.autocode.spring.project.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private ModelMapper modelMapper;

    @InjectMocks private EmployeeServiceImpl employeeService;

    @Test
    void addEmployee_ShouldSetRoleAndEncodePassword() {
        // Arrange
        String rawPassword = "plainPass";
        String email = "emp@test.com";

        EmployeeDTO dto = new EmployeeDTO();
        dto.setPassword(rawPassword);
        dto.setEmail(email);

        // This is the entity the ModelMapper will "return" when called
        Employee employee = new Employee();
        employee.setPassword(rawPassword); // [FIX] Must set password here so it's not null in service
        employee.setEmail(email);

        // Mocks
        // 1. Ensure email doesn't exist
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.empty());

        // 2. Return our prepared employee object
        when(modelMapper.map(dto, Employee.class)).thenReturn(employee);

        // 3. Stub the encoder to expect "plainPass" (which is now in the object)
        when(passwordEncoder.encode(rawPassword)).thenReturn("encodedPass");

        // 4. Return the saved employee
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // 5. Return DTO at end
        when(modelMapper.map(employee, EmployeeDTO.class)).thenReturn(dto);

        // Act
        employeeService.addEmployee(dto);

        // Assert
        assertEquals("encodedPass", employee.getPassword()); // Verify the service updated the entity
        verify(employeeRepository).save(employee);
    }
}