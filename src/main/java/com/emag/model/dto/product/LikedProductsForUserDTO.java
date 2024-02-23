package com.emag.model.dto.product;

import com.emag.model.pojo.Product;
import com.emag.model.pojo.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class LikedProductsForUserDTO {
    private List<ResponseProductDTO> likedProducts;

}
