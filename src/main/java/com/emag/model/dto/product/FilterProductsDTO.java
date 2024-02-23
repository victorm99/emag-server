package com.emag.model.dto.product;

import lombok.*;

@Data
public class FilterProductsDTO {
    private Long subcategoryId;
    private String searchKeyword;
    private String brand;
    private String model;
    private Double maxPrice;
    private Double minPrice;
    private Boolean discountedOnly;
    private Boolean orderByPrice;
    private Boolean sortDesc;
    private Integer productsPerPage;
    private Integer pageNumber;
}
