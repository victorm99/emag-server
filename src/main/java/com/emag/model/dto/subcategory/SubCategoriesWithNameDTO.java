package com.emag.model.dto.subcategory;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SubCategoriesWithNameDTO {

    private Long id;
    @NotBlank(message = "Subcategory is mandatory")
    @Size(min = 2, max = 45, message = "Subcategory must be between 2 and 45 symbols.")
    private String name;
}
