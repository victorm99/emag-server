package com.emag.model.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class DiscountDTO {
    @NotNull(message = "Discount percent must be between 1 and 95")
    @Min(value=1, message="Discount percent must be between 1 and 95")
    @Max(value = 95, message = "Discount percent must be between 1 and 95")
    private Integer discountPercent;
    @NotNull
    private Timestamp expireDate;
}
