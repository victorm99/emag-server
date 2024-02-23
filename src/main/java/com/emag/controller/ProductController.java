package com.emag.controller;

import com.emag.exception.BadRequestException;
import com.emag.exception.UnauthorizedException;
import com.emag.model.dao.ProductDAO;
import com.emag.model.dto.product.FilterProductsDTO;
import com.emag.model.dto.product.LikedProductsForUserDTO;
import com.emag.model.dto.product.RequestProductDTO;
import com.emag.model.dto.product.ResponseProductDTO;
import com.emag.model.pojo.Product;
import com.emag.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
public class ProductController {

    @Autowired
    SessionManager sessionManager;

    @Autowired
    ProductService productService;

    @Autowired
    ProductDAO productDAO;


    @PostMapping("/products")
    public ResponseEntity<ResponseProductDTO> addProduct(@Valid @RequestBody RequestProductDTO p, BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()){
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        if(!sessionManager.userHasPrivileges(request)){
            throw new UnauthorizedException("Not admin!");
        }
        return ResponseEntity.ok(productService.addProduct(p));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ResponseProductDTO> editProduct(@PathVariable long id , @RequestBody RequestProductDTO p , HttpServletRequest request){
        if(!sessionManager.userHasPrivileges(request)){
            throw new UnauthorizedException("Not admin!");
        }
        return ResponseEntity.ok(productService.editProduct(p , id));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<ResponseProductDTO> deleteProduct(@PathVariable long id , HttpServletRequest request){
        if(!sessionManager.userHasPrivileges(request)){
            throw new UnauthorizedException("Not admin!");
        }
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ResponseProductDTO> getProductById (@PathVariable long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping("/products/{id}/fav")
    public ResponseEntity<LikedProductsForUserDTO> addProductToFavourites (@PathVariable long id , HttpServletRequest request){
        return ResponseEntity.ok(productService.addProductToFavourites(id , sessionManager.getLoggedUser(request)));
    }

    @DeleteMapping("/products/{id}/fav")
    public ResponseEntity<LikedProductsForUserDTO> removeProductFromFavourites (@PathVariable long id , HttpServletRequest request){
        return ResponseEntity.ok(productService.removeProductFromFavourites(id , sessionManager.getLoggedUser(request)));
    }

    @GetMapping("/subcategories/{id}/products")
    public List<ResponseProductDTO> getProductsBySubcategory(@PathVariable long id , HttpSession session){
        sessionManager.setSubcategoryId(session , id);
        return productService.getProductsBySubcategory(id);
    }

    @GetMapping("/subcategories/products/{sortedBy}")
    public List<ResponseProductDTO> getProductsBySubcategorySortedBy (@PathVariable String sortedBy , HttpServletRequest request){
        return productService.getProductsBySubcategorySortedBy (sessionManager.getSubcategoryId(request) , sortedBy);
    }


    @GetMapping("/subcategories/products/{min}/{max}")
    public List<Product> getProductsBetween (@PathVariable double min, @PathVariable double max, HttpServletRequest request){
        return productService.getProductsBetween (sessionManager.getSubcategoryId(request), min, max);
    }

    @PostMapping("/products/filter")
    public List<ResponseProductDTO> filterProducts (@RequestBody FilterProductsDTO dto){
        return productService.filterProducts(dto);
    }

    @GetMapping("/products/search/{keywordSequence}")
    public ResponseEntity<Page<ResponseProductDTO>> searchProductsByKeyword(@PathVariable String keywordSequence , Pageable pageable){
        return ResponseEntity.ok(productService.searchProductsByKeyword(pageable , keywordSequence));
    }

    @GetMapping("/products/fav")
    ResponseEntity<List<ResponseProductDTO>> getAllFavouriteProducts(HttpServletRequest request){
        if (!sessionManager.userHasPrivileges(request , sessionManager.getLoggedUser(request).getId())){
            throw new UnauthorizedException("No privileges!");
        }
        return ResponseEntity.ok(productService.getAllFavouriteProducts(sessionManager.getLoggedUser(request)));
    }

    @PostMapping("/products/{id}/image")
    public String addImage(@RequestPart MultipartFile file, @PathVariable long id, HttpServletRequest request) {
        if(!sessionManager.userHasPrivileges(request)){
            throw new UnauthorizedException("Not admin!");
        }
        return productService.addImage(file,id);
    }

}
