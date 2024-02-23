package com.emag.controller;
import com.emag.exception.UnauthorizedException;
import com.emag.model.dto.category.EditCategoryDTO;
import com.emag.model.dto.category.CategoryWithoutIdDTO;
import com.emag.model.pojo.Category;
import com.emag.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController

public class CategoryController {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/categories")
    public ResponseEntity<Category> addCategory (@RequestBody @Valid CategoryWithoutIdDTO c, HttpServletRequest request){
        if (!sessionManager.userHasPrivileges(request)){
            throw new UnauthorizedException("No permission!");
        }
        return ResponseEntity.ok(categoryService.addCategory(c));
    }

    @PutMapping("/categories")
    public ResponseEntity<Category> editCategory (@RequestBody @Valid EditCategoryDTO c, HttpServletRequest request){
        if (!sessionManager.userHasPrivileges(request)){
            throw new UnauthorizedException("No permission!");
        }
        return ResponseEntity.ok(categoryService.editCategory(c));
    }

    @GetMapping("/categories")
    public List<CategoryWithoutIdDTO> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @DeleteMapping("/categories")
    public CategoryWithoutIdDTO deleteCategory(@RequestBody @Valid CategoryWithoutIdDTO categoryWithoutId, HttpServletRequest request){
        if (!sessionManager.userHasPrivileges(request)){
            throw new UnauthorizedException("No permission!");
        }
        return categoryService.deleteCategory(categoryWithoutId);
    }
}
