package com.emag.model.dto.category;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CategoryWithoutIdDTO {

    private Long id;
    @NotBlank(message = "Category is mandatory")
    @Size(min = 2, max = 45, message = "Category must be between 2 and 45 symbols.")
    private String name;
}
