package com.emag.controller;

import com.emag.model.dto.product.ResponseProductDTO;
import com.emag.model.pojo.UserCart;
import com.emag.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class CartController {

    @Autowired
    SessionManager sessionManager;

    @Autowired
    CartService cartService;

    @PostMapping("/cart/{productId}")
    public ResponseEntity<UserCart> addProductToCart(@PathVariable long productId , HttpServletRequest request){
        return ResponseEntity.ok(cartService.addProductToCart(productId , sessionManager.getLoggedUser(request)));
    }

    @DeleteMapping("/cart/{productId}")
    public ResponseEntity<UserCart> removeProductFromCart(@PathVariable long productId , HttpServletRequest request){
        return ResponseEntity.ok(cartService.removeProductFromCart(productId , sessionManager.getLoggedUser(request)));
    }

    @GetMapping("/cart")
    public ResponseEntity<List<ResponseProductDTO>> getAllProductsFromCart (HttpServletRequest request){
        return ResponseEntity.ok(cartService.getAllProductsFromCart(sessionManager.getLoggedUser(request)));
    }

}
