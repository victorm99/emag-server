package com.emag.util;

import com.emag.exception.BadRequestException;
import com.emag.model.pojo.SubCategory;
import com.emag.model.repository.SubCategoryRepository;

public class SubCategoryUtil {
    public static void validateSubCategory(String name, SubCategoryRepository subCategoryRepository){
        if (name.trim().length()<=1 || name.trim().length()>45){
            throw new BadRequestException("Wrong credentials!");
        }
        if (subCategoryRepository.findBySubcategoryName(name) != null){
            throw new BadRequestException("Subcategory already exists!");
        }
    }

    public static void validateSubCategoryExists(String name, SubCategoryRepository subCategoryRepository){
        if (subCategoryRepository.findBySubcategoryName(name) == null){
            throw new BadRequestException("SubCategory don't exists!");
        }
    }
}
