package com.emag.model.dto.review;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class DoReviewDTO {

    @NotBlank(message = "Title is mandatory")
    @Size(min = 2, max = 45, message = "Tittle must be between 2 and 45 symbols")
    private String title;
    @NotBlank(message = "Description is mandatory")
    private String description;
    @Min(value = 1, message = "Review rating should not be less than 1")
    @Max(value = 5, message = "Review rating should not be greater than 5")
    private Integer rating;
}
