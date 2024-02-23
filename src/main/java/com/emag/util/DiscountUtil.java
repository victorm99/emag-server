package com.emag.util;

import com.emag.exception.BadRequestException;
import com.emag.model.dto.DiscountDTO;
import com.emag.model.pojo.Discount;
import com.emag.model.pojo.Product;
import com.emag.model.pojo.User;
import com.emag.model.repository.UserRepository;
import com.emag.service.EmailService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class DiscountUtil {

    public static void sendMail(UserRepository userRepository, Product product, Discount discount, EmailService emailService){
        List<User> users = userRepository.findAllBySubscribedIsTrue();
        String mailText = product.getName()+ " will be on discount since: "
                +discount.getStartDate()+" and will be "+discount.getDiscountPercent()+
                "% down of the price. " + "Discount ends at: " + discount.getExpireDate() +
                ". You can find the product on: http://localhost:9999/products/"+product.getId();
        users.forEach(user -> emailService.sendSimpleMessage(user.getEmail(),
                "New discount", mailText));
    }

    public static void validateDiscountRequest(int discountPercent , Timestamp startDate , Timestamp expireDate){
        if(discountPercent <= 0){
            throw new BadRequestException("Enter a valid discount percent");
        }
        if (expireDate != null) {
            if (expireDate.toLocalDateTime().isBefore(startDate.toLocalDateTime())) {
                throw new BadRequestException("Enter a valid expiration date");
            }
        }
    }
}
