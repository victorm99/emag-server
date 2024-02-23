package com.emag.model.dto;

import com.emag.model.pojo.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class OrderDTO {
    private List<Map.Entry<Product , Integer>> order;
    private double totalSum;
}
