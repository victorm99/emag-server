package com.emag.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Component
@Entity
@Table(name = "discounts")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int discountPercent;
    private Timestamp startDate;
    private Timestamp expireDate;

    @OneToMany(mappedBy = "discount")
    @JsonBackReference
    List<Product> products;

}
