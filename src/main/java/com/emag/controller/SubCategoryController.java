package com.emag.controller;

import com.emag.exception.UnauthorizedException;
import com.emag.model.dto.category.EditCategoryDTO;
import com.emag.model.dto.subcategory.AddSubCategoryDTO;
import com.emag.model.dto.subcategory.SubCategoriesWithNameDTO;
import com.emag.model.pojo.SubCategory;
import com.emag.model.repository.SubCategoryRepository;
import com.emag.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
public class SubCategoryController {

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private SubCategoryService subCategoryService;
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @PostMapping("/subcategories")
    public ResponseEntity<SubCategory> addSubCategory (@RequestBody @Valid AddSubCategoryDTO c, HttpServletRequest request){
        if (!sessionManager.userHasPrivileges(request)){
            throw new UnauthorizedException("No permission!");
        }
        return ResponseEntity.ok(subCategoryService.addSubCategory(c));
    }

    @PutMapping("/subcategories")
    public ResponseEntity<SubCategory> editSubCategory (@RequestBody @Valid EditCategoryDTO c, HttpServletRequest request){
        if (!sessionManager.userHasPrivileges(request)){
            throw new UnauthorizedException("No permission!");
        }
        return ResponseEntity.ok(subCategoryService.editSubCategory(c));
    }

    @GetMapping("/subcategories/{id}")
    public List<SubCategoriesWithNameDTO> getSubCategoriesFromCategory(@PathVariable long id){
        return subCategoryService.getSubCategoriesFromCategory(id);
    }

    @GetMapping("/subcategories/findAll")
    public List<SubCategory> getAll() {
        return subCategoryRepository.findAll();
    }

    @DeleteMapping("/subcategories")
    public SubCategoriesWithNameDTO deleteSubCategory(@RequestBody @Valid SubCategoriesWithNameDTO s, HttpServletRequest request){
        if (!sessionManager.userHasPrivileges(request)){
            throw new UnauthorizedException("No permission!");
        }
        return subCategoryService.deleteCategory(s);
    }
}
