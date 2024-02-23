package com.emag.model.pojo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders_have_products")
public class OrderedProduct {

    @Embeddable
    @Data
    public static class OrderedProductsKey implements Serializable {
        long orderId;
        long productId;
    }

    @EmbeddedId
    private OrderedProductsKey primaryKey;

    @ManyToOne
    @MapsId("orderId")
    @JsonManagedReference
    @JoinColumn(name = "order_id")
    Order order;

    @ManyToOne
    @MapsId("productId")
    @JsonManagedReference
    @JoinColumn(name = "product_id")
    Product product;

    private int quantity;

}
