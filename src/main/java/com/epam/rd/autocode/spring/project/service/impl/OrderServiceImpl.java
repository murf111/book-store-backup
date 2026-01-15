package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.model.BookItem;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.model.Order;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.repo.OrderRepository;
import com.epam.rd.autocode.spring.project.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<OrderDTO> getOrdersByClient(String clientEmail) {
        return orderRepository.findAllByClientEmail(clientEmail)
                              .stream()
                              .map(order -> modelMapper.map(order, OrderDTO.class))
                              .toList();
    }

    @Override
    public List<OrderDTO> getOrdersByEmployee(String employeeEmail) {
        return orderRepository.findAllByEmployeeEmail(employeeEmail)
                              .stream()
                              .map(order -> modelMapper.map(order, OrderDTO.class))
                              .toList();
    }

    @Override
    @Transactional
    public OrderDTO addOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());

        Client client = clientRepository.findByEmail(orderDTO.getClientEmail())
                                        .orElseThrow(() -> new NotFoundException
                                                ("Client not found: " + orderDTO.getClientEmail()));
        order.setClient(client);

        if (orderDTO.getEmployeeEmail() != null) {
            Employee employee = employeeRepository.findByEmail(orderDTO.getEmployeeEmail())
                                                  .orElseThrow(() -> new NotFoundException
                                                          ("Employee not found: " + orderDTO.getEmployeeEmail()));
            order.setEmployee(employee);
        }

        List<BookItem> bookItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        if (orderDTO.getBookItems() != null) {
            for (BookItemDTO bookItemDTO : orderDTO.getBookItems()) {
                Book book = bookRepository.findByName(bookItemDTO.getBookName())
                                          .orElseThrow(() -> new NotFoundException
                                                          ("Book with name " +
                                                           bookItemDTO.getBookName() +
                                                           " was not found"));
                BookItem bookItem = new BookItem();
                bookItem.setBook(book);
                bookItem.setOrder(order);
                bookItem.setQuantity(bookItemDTO.getQuantity());

                bookItems.add(bookItem);

                BigDecimal itemCost = book.getPrice().multiply(BigDecimal.valueOf(bookItemDTO.getQuantity()));
                totalPrice = totalPrice.add(itemCost);
            }
        }
        order.setBookItems(bookItems);
        order.setPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        return modelMapper.map(savedOrder, OrderDTO.class);
    }
}
