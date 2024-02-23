package com.emag.service;


import com.emag.exception.BadRequestException;
import com.emag.model.dto.category.EditCategoryDTO;
import com.emag.model.pojo.Category;
import com.emag.util.CategoryUtil;
import org.springframework.stereotype.Service;
import com.emag.model.dto.category.CategoryWithoutIdDTO;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService extends AbstractService {


    public Category addCategory(CategoryWithoutIdDTO c) {
        CategoryUtil.validateName(c.getName(), categoryRepository);
        Category category = modelMapper.map(c,Category.class);
        return categoryRepository.save(category);
    }

    public Category editCategory(EditCategoryDTO c) {
        String oldName = c.getOldName();
        String newName = c.getNewName();
        CategoryUtil.validateNameExists(oldName, categoryRepository);
        CategoryUtil.validateName(newName, categoryRepository);
        Category category = categoryRepository.findByCategoryName(oldName);
        category.setCategoryName(newName);
        return categoryRepository.save(category);
    }

    public List<CategoryWithoutIdDTO> getAllCategories() {
        List<CategoryWithoutIdDTO> categoriesWithoutId = new ArrayList<>();
        List<Category> categories = categoryRepository.findAll();
        categories.forEach(category ->
                categoriesWithoutId.add(modelMapper.map(category, CategoryWithoutIdDTO.class)));
        return categoriesWithoutId;
    }

    public CategoryWithoutIdDTO deleteCategory(CategoryWithoutIdDTO categoryWithoutId) {
        String categoryName = categoryWithoutId.getName();
        CategoryUtil.validateNameExists(categoryName, categoryRepository);
        Category category = categoryRepository.findByCategoryName(categoryName);
        categoryRepository.delete(category);
        return categoryWithoutId;
    }
}