package com.emag.controller;

import com.emag.model.dto.OrderDTO;
import com.emag.model.pojo.Order;
import com.emag.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    SessionManager sessionManager;

    @Autowired
    OrderService orderService;

    @Transactional
    @PostMapping("/orders")
    OrderDTO createOrder (HttpServletRequest request) {
        return orderService.createOrder(sessionManager.getLoggedUser(request));
    }

    @GetMapping("/orders")
    List<Order> getAllOrders (HttpServletRequest request){
        return orderService.getAllOrders(sessionManager.getLoggedUser(request));
    }

}
