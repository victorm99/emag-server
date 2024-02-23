package com.emag.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;

@Component
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_has_products_in_cart")
public class UserCart {

    @Embeddable
    @Data
    public static class UserCartKey implements Serializable {
        @Column(name = "user_id")
        private long userId;
        @Column(name = "product_id")
        private long productId;
    }

    @EmbeddedId
    private UserCartKey primaryKey;

    @ManyToOne
    @JsonIgnore
//    @JsonManagedReference
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JsonManagedReference
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;

    private int quantity;

}
