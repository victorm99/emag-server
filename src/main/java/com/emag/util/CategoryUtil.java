package com.emag.util;

import com.emag.exception.BadRequestException;
import com.emag.model.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;


public class CategoryUtil {

    public static void validateName(String name, CategoryRepository categoryRepository){
        if (name.trim().length()<=1 || name.trim().length()>45){
            throw new BadRequestException("Wrong credentials!");
        }
        if (categoryRepository.findByCategoryName(name) != null){
            throw new BadRequestException("Category already exists!");
        }
    }

    public static void validateNameExists(String name, CategoryRepository categoryRepository){
        if (categoryRepository.findByCategoryName(name) == null){
            throw new BadRequestException("The category don't exists!");
        }
    }
}
