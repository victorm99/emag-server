package com.emag.service;

import com.emag.exception.BadRequestException;
import com.emag.exception.NotFoundException;
import com.emag.model.dto.product.ResponseProductDTO;
import com.emag.model.pojo.Product;
import com.emag.model.pojo.User;
import com.emag.model.pojo.UserCart;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService extends AbstractService{

    public UserCart addProductToCart(long id, User user) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found !"));
        if (product.getDeletedAt() != null){
            throw new BadRequestException("Product deleted");
        }
        int productAvailableQuantity = product.getQuantity();
        UserCart.UserCartKey primaryKey = new UserCart.UserCartKey();
        primaryKey.setProductId(id);
        primaryKey.setUserId(user.getId());
        UserCart userCart = cartRepository.findByPrimaryKey(primaryKey).orElse(null);
        if (userCart != null){
            int orderedQuantity = userCart.getQuantity();
            if (productAvailableQuantity <= orderedQuantity){
                throw new BadRequestException("Product not available!");
            }
            else {
                userCart.setQuantity(userCart.getQuantity() + 1);
                return cartRepository.save(userCart);
            }
        }
        return cartRepository.save(new UserCart(primaryKey , user , product , 1));
    }

    public UserCart removeProductFromCart(long productId, User user) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found !"));
        UserCart.UserCartKey primaryKey = new UserCart.UserCartKey();
        primaryKey.setProductId(productId);
        primaryKey.setUserId(user.getId());
        UserCart userCart = cartRepository.findByPrimaryKey(primaryKey)
                .orElseThrow(() -> new NotFoundException("Product was not added to cart!"));
        userCart.setQuantity(userCart.getQuantity() - 1);
        if (userCart.getQuantity() > 0) {
            return cartRepository.save(userCart);
        }
        cartRepository.delete(userCart);
        return null;
    }

    public List<ResponseProductDTO> getAllProductsFromCart(User user) {
        List<UserCart> userCarts = cartRepository.findAllByUser(user).orElseThrow(() -> new NotFoundException("Carts empty!"));
        if (userCarts.isEmpty()){
            throw new NotFoundException("Carts empty!");
        }
        List<ResponseProductDTO> productDTO = new ArrayList<>();
        userCarts.forEach(userCart -> {
            ResponseProductDTO dto = modelMapper.map(userCart.getProduct() , ResponseProductDTO.class);
            dto.setQuantity(userCart.getQuantity());
            productDTO.add(dto);
        });
        return productDTO;
    }
}
