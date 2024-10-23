package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exception.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderStatus;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.repositories.UserRepository;
import com.project.shopapp.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        //tim xem user'id co ton tai khong
        User existingUser = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Can not find user with id + " + orderDTO.getUserId()));
        //conert DTO -> Order
        //dung thu vien Model Mapper
        //Tao mot luong banh anh xa rieng de kiem soat viec anh xa
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderDTO, order);

        order.setUser(existingUser);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);

        //Kiem tra shipping date >= now
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Date must be at least today !");
        }
        order.setActive(true);
        order.setShippingDate(shippingDate);

        orderRepository.save(order);
        return order;
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        User existingUser = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Can not find user with id + " + orderDTO.getUserId()));
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Can not find order with id + " + id));

        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO, order);
        order.setUser(existingUser);
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
