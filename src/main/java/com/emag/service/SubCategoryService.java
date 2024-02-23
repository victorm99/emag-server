package com.emag.service;

import com.emag.exception.BadRequestException;
import com.emag.exception.NotFoundException;
import com.emag.model.dto.category.EditCategoryDTO;
import com.emag.model.dto.category.CategoryWithoutIdDTO;
import com.emag.model.dto.subcategory.AddSubCategoryDTO;
import com.emag.model.dto.subcategory.SubCategoriesWithNameDTO;
import com.emag.model.pojo.Category;
import com.emag.model.pojo.SubCategory;
import com.emag.util.CategoryUtil;
import com.emag.util.SubCategoryUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubCategoryService extends AbstractService{


    public SubCategory addSubCategory(AddSubCategoryDTO c) {
        String subCategoryName = c.getSubCategoryName();
        String categoryName = c.getCategoryName();
        CategoryUtil.validateNameExists(categoryName, categoryRepository);
        SubCategoryUtil.validateSubCategory(subCategoryName, subCategoryRepository);
        SubCategory subCategory = new SubCategory();
        subCategory.setSubcategoryName(subCategoryName);
        subCategory.setCategory(categoryRepository.findByCategoryName(categoryName));
        return subCategoryRepository.save(subCategory);
    }

    public SubCategory editSubCategory(EditCategoryDTO c) {
        String oldName = c.getOldName();
        String newName = c.getNewName();
        SubCategoryUtil.validateSubCategoryExists(oldName, subCategoryRepository);
        SubCategoryUtil.validateSubCategory(newName, subCategoryRepository);
        SubCategory subCategory = subCategoryRepository.findBySubcategoryName(oldName);
        subCategory.setSubcategoryName(newName);
        return subCategoryRepository.save(subCategory);
    }

    public List<SubCategoriesWithNameDTO> getSubCategoriesFromCategory(long id) {
        List<SubCategoriesWithNameDTO> subCategoriesWithNameDTOS = new ArrayList<>();
        List<SubCategory> subCategories = subCategoryRepository.findAllByCategoryId(id);
        subCategories.forEach(subCategory ->
                subCategoriesWithNameDTOS.add(modelMapper.map(subCategory, SubCategoriesWithNameDTO.class)));
        return subCategoriesWithNameDTOS;
    }


    public SubCategoriesWithNameDTO deleteCategory(SubCategoriesWithNameDTO s) {
        String subCategoryName = s.getName();
        SubCategoryUtil.validateSubCategoryExists(subCategoryName, subCategoryRepository);
        SubCategory subCategory = subCategoryRepository.findBySubcategoryName(subCategoryName);
        subCategoryRepository.delete(subCategory);
        return s;
    }
}
