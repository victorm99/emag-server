package com.emag.service;

import com.emag.exception.BadRequestException;
import com.emag.exception.NotFoundException;
import com.emag.model.dto.OrderDTO;
import com.emag.model.pojo.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService extends AbstractService{

    public OrderDTO createOrder(User user) {
        List<UserCart> cart = cartRepository.findAllByUser(user).orElse(null);
        if (cart.isEmpty() || cart == null){
            throw new BadRequestException("Carts empty!");
        }

        Order order = new Order();
        order.setBuyer(user);
        order.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        orderRepository.save(order);

        OrderedProduct.OrderedProductsKey primaryKey = new OrderedProduct.OrderedProductsKey();
        primaryKey.setOrderId(order.getId());


        HashMap<Product , Integer> productsQuantities = new HashMap<>();
        cart.forEach(userCart -> {
            Product product = userCart.getProduct();
            if (product.getDeletedAt() != null){
                throw new BadRequestException("Product deleted !");
            }
            primaryKey.setProductId(product.getId());
            int availableProductQuantity = product.getQuantity();
            int orderedQuantity = userCart.getQuantity();
            if (availableProductQuantity < orderedQuantity){
                orderRepository.deleteById(order.getId());
                throw new BadRequestException("Limited product quantity!");
            }
            productsQuantities.put(product , orderedQuantity );
            product.setQuantity(product.getQuantity() - orderedQuantity);

            orderedProductRepository.save(new OrderedProduct(primaryKey , order , product , orderedQuantity));
            if (product.getQuantity() <= 0){
                product.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
                productRepository.deleteById(product.getId());
            }
            cartRepository.delete(userCart);
        });
        double totalSum = productsQuantities.entrySet().stream()
                .map(productIntegerEntry -> {
                    Product product = productIntegerEntry.getKey();
                    int quantity = productIntegerEntry.getValue();
                    if (product.getDiscountedPrice() != 0){
                        return product.getDiscountedPrice()*quantity;
                    }
                    return product.getPrice()*quantity;
                })
                .reduce(Double::sum).orElse(0.0);
        return new OrderDTO(new ArrayList<>(productsQuantities.entrySet()) , totalSum);
    }

    public List<Order> getAllOrders(User user) {
        return orderRepository.findAllByBuyer(user).orElseThrow(() -> new BadRequestException("User has no orders!"));
    }
}
