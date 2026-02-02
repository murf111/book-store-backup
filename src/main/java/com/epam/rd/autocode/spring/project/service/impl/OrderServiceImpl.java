package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.model.BookItem;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.model.Order;
import com.epam.rd.autocode.spring.project.model.enums.OrderStatus;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.repo.OrderRepository;
import com.epam.rd.autocode.spring.project.service.CartService;
import com.epam.rd.autocode.spring.project.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link OrderService}.
 * <p>
 * Handles the whole lifecycle of orders, including creation and price calculation.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final CartService cartService;

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                              .stream()
                              .map(order -> modelMapper.map(order, OrderDTO.class))
                              .toList();
    }

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

    /**
     * Creates a new order in the system.
     * <p>
     * <strong>Business Logic:</strong>
     * <ul>
     * <li>Verifies the existence of the Client.</li>
     * <li>Iterates through requested Book items to verify existence and fetch current prices.</li>
     * <li>Calculates the total price server-side to prevent client-side price manipulation.</li>
     * <li>Sets the {@code orderDate} to the current server timestamp.</li>
     * <li>Initially sets the assigned Employee to {@code null} (Pending state).</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Transaction:</strong> Marked as {@code @Transactional} to ensure that the Order
     * and all its BookItems are saved atomically. If any book is not found, the entire
     * operation rolls back.
     * </p>
     *
     * @param orderDTO data transfer object containing client email and list of books.
     * @return the persisted Order converted to DTO.
     * @throws NotFoundException if the client or any of the books cannot be found in the database.
     */
    @Override
    @Transactional
    public OrderDTO addOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());

        Client client = clientRepository.findByEmail(orderDTO.getClientEmail())
                                        .orElseThrow(() -> new NotFoundException
                                                ("Client not found: " + orderDTO.getClientEmail()));
        order.setClient(client);
        order.setEmployee(null);

        order.setDeliveryAddress(orderDTO.getDeliveryAddress());
        order.setCity(orderDTO.getCity());
        order.setPostalCode(orderDTO.getPostalCode());

        List<BookItem> bookItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        if (orderDTO.getBookItems() != null) {
            for (BookItemDTO bookItemDTO : orderDTO.getBookItems()) {
                Book book = bookRepository.findById(bookItemDTO.getBookId())
                                          .orElseThrow(() -> new NotFoundException
                                                          ("Book " + bookItemDTO.getBookId() + " was not found"));
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

        if (client.getRole() == com.epam.rd.autocode.spring.project.model.enums.Role.CLIENT) {
            if (client.getBalance().compareTo(totalPrice) < 0) {
                throw new RuntimeException("Insufficient funds! Balance: " + client.getBalance() + ", Total: " + totalPrice);
            }
            // Deduct money
            client.setBalance(client.getBalance().subtract(totalPrice));
            clientRepository.save(client); // Save the new balance
        }

        Order savedOrder = orderRepository.save(order);

        return modelMapper.map(savedOrder, OrderDTO.class);
    }

    @Override
    @Transactional
    public OrderDTO confirmOrder(Long orderId, String employeeEmail) {
        Order order = orderRepository.findById(orderId)
                                     .orElseThrow(() -> new NotFoundException
                                             ("Order not found with id: " + orderId));

        Employee employee = employeeRepository.findByEmail(employeeEmail)
                                                        .orElseThrow(() -> new NotFoundException
                                                                ("Employee not found with email: " + employeeEmail));
        order.setStatus(OrderStatus.CONFIRMED);
        order.setEmployee(employee);
        Order savedOrder = orderRepository.save(order);

        return modelMapper.map(savedOrder, OrderDTO.class);
    }
}
