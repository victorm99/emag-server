package com.emag.model.repository;

import com.emag.model.pojo.Order;
import com.emag.model.pojo.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct , OrderedProduct.OrderedProductsKey> {
    Optional<List<OrderedProduct>> findAllByOrder (Order order);
}
