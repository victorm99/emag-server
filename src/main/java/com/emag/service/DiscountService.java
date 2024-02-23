package com.emag.service;

import com.emag.exception.BadRequestException;
import com.emag.exception.NotFoundException;
import com.emag.model.dto.DiscountDTO;
import com.emag.model.pojo.Discount;
import com.emag.model.pojo.Product;
import com.emag.model.pojo.User;
import com.emag.util.DiscountUtil;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiscountService extends AbstractService{

    public Discount createDiscount(DiscountDTO dto , long id){
        int discountPercent = dto.getDiscountPercent();
        Timestamp startDate = Timestamp.valueOf(LocalDateTime.now());
        Timestamp expireDate = dto.getExpireDate();
        DiscountUtil.validateDiscountRequest(discountPercent , startDate , expireDate);
//        If requested discount equals discount from DB -> return discount from DB , else create new discount
        Discount discount = discountRepository.findDiscountByDiscountPercentAndStartDateAndExpireDate
                (discountPercent, startDate , expireDate).orElse(
                        new Discount().builder()
                        .discountPercent(discountPercent)
                        .startDate(startDate)
                        .expireDate(expireDate)
                        .build());
//        Set discount and discounted price to product
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found!"));
        double price = product.getPrice();
        double discountedPrice = price - price*discountPercent/100;
        product.setDiscount(discount);
        product.setDiscountedPrice(discountedPrice);
        new Thread(() ->
                DiscountUtil.sendMail(userRepository, product, discount, emailService)).start();
        return discountRepository.save(discount);
    }
}
