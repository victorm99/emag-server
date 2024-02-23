package com.emag.util;

import com.emag.exception.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

public class ImageUtil {

    private static final String[] ACCEPTED_IMAGE_FORMATS = {"jpeg" , "png" , "jpg"};
    private static final long MAX_SIZE_OF_IMAGE = 9*1000000;

    public static String validateImageAndReturnName(MultipartFile file) throws IOException {

        if (file.getBytes().length>MAX_SIZE_OF_IMAGE){
            throw new BadRequestException("Image is too large");
        }
        String[] strings = file.getOriginalFilename().split("\\.");
        String extension = strings[strings.length-1];
        if (!Arrays.asList(ACCEPTED_IMAGE_FORMATS).contains(extension)){
            throw new BadRequestException("Unsupported file format !");
        }
        return System.nanoTime() + "." + extension;
    }
}
