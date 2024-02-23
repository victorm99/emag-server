package com.emag.model.dto.product;

import com.emag.model.pojo.SubCategory;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestProductDTO {
    @NotBlank(message = "Product name is mandatory!")
    @Size(min = 2, max = 45,message = "Name should be between 2 and 45 symbols")
    private String name;
    @NotNull(message = "Product sub category is mandatory!")
    @Column(name = "sub_category_id")
    private long subCategoryId;
    @NotBlank(message = "Product brand is mandatory!")
    @Size(min = 2, max = 45, message = "Name should be between 2 and 45 symbols")
    private String brand;
    @NotBlank(message = "Product model is mandatory!")
    @Size(min = 2, max = 45, message = "Name should be between 2 and 45 symbols")
    private String model;

    @NotNull(message = "Product price is mandatory!")
    @Min(value = 1, message = "Product price must be positive number!")
    private double price;

    @NotNull(message = "Product quantity is mandatory!")
    @Min(value = 1, message = "Product quantity must be positive number!")
    private int quantity;

    private String description;
    private int warrantyMonths;
}
