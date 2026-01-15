package com.epam.rd.autocode.spring.project.conf;

import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Employee> employee = employeeRepository.findByEmail(email);
        if (employee.isPresent()) {
            return new User(employee.get().getEmail(),
                            employee.get().getPassword(),
                            List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE")));
        }

        Optional<Client> client = clientRepository.findByEmail(email);
        if (client.isPresent()) {
            return new User(client.get().getEmail(),
                            client.get().getPassword(),
                            List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
