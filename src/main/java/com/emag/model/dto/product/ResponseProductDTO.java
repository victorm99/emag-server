package com.emag.model.dto.product;

import com.emag.model.pojo.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Component
public class ResponseProductDTO {
    private long id;
    private String name;
    private SubCategory subCategory;
    private String brand;
    private String model;
    private double price;
    private double discountedPrice;
    private String description;
    private int quantity;
    private int warrantyMonths;
    private Timestamp addedAt;
    private Timestamp deletedAt;
    private double productRating;
    private Discount discount;
    private List<ProductImage> productImages;
}
