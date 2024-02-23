package com.emag.model.dto.category;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class EditCategoryDTO {
    @NotBlank(message = "Old category is mandatory")
    @Size(min = 2, max = 45, message = "Category must be between 2 and 45 symbols.")
    private String oldName;
    @NotBlank(message = "New category is mandatory")
    @Size(min = 2, max = 45, message = "Category must be between 2 and 45 symbols.")
    private String newName;
}
