package com.emag.util;

import com.emag.exception.BadRequestException;
import com.emag.model.pojo.Product;
import com.emag.service.ProductService;

public class ProductUtil {

    public static void validateDescription(String desc){
        if (desc.isBlank() || desc.trim().length()<=1){
            throw new BadRequestException("Wrong credentials!");
        }
    }

    public static void validateString(String smth){
        if (smth.isBlank() || smth.trim().length()<=1 || smth.trim().length()>45){
            throw new BadRequestException("Wrong credentials!");
        }
    }

    public static void validateInt(int smth){
        if (smth <= 0 ){
            throw new BadRequestException("Wrong credentials");
        }
    }

    public static void validateDouble(double smth){
        if (smth <= 0 ){
            throw new BadRequestException("Wrong credentials");
        }
    }
}
