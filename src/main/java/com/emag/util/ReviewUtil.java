package com.emag.util;

import com.emag.exception.BadRequestException;
import com.emag.model.dto.review.DoReviewDTO;

public class ReviewUtil {
    public static void validateReview(DoReviewDTO r){
        if (r.getDescription().trim().length()<=1){
            throw new BadRequestException("Invalid description!");
        }
        if (r.getTitle().trim().length()<=1 || r.getTitle().trim().length()>45){
            throw new BadRequestException("Invalid title!");
        }
        if (r.getRating()<1||r.getRating()>5){
            throw new BadRequestException("Invalid rating!");
        }
    }
}
