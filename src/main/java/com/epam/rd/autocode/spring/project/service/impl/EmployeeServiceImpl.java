package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.repository.EmployeeRepository;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                                 .stream()
                                 .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                                 .toList();
    }

    @Override
    public EmployeeDTO getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email)
                                 .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                                 .orElseThrow(() -> new NotFoundException
                                       ("Employee with email " + email + " was not found"));
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployeeByEmail(String email, EmployeeDTO employee) {
        Employee existingEmployee = employeeRepository.findByEmail(email)
                                                  .orElseThrow(() -> new NotFoundException
                                                        ("Employee with email " + email + " was not found"));

        modelMapper.map(employee, existingEmployee);

        Employee savedEmployee = employeeRepository.save(existingEmployee);

        return modelMapper.map(savedEmployee, EmployeeDTO.class);
    }

    @Override
    @Transactional
    public void deleteEmployeeByEmail(String email) {
        employeeRepository.delete(employeeRepository.findByEmail(email)
                                                    .orElseThrow(() -> new NotFoundException
                                                            ("Employee with email " + email + " was not found")));
    }

    @Override
    @Transactional
    public EmployeeDTO addEmployee(EmployeeDTO employee) {
        if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            throw new AlreadyExistException("Employee already exists with email: " + employee.getEmail());
        }
        Employee newEmployee = modelMapper.map(employee, Employee.class);

        Employee savedEmployee = employeeRepository.save(newEmployee);
        return modelMapper.map(savedEmployee, EmployeeDTO.class);
    }
}
