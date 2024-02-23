package com.emag.service;

import com.emag.model.pojo.Discount;
import com.emag.model.pojo.Product;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;


@Configuration
@EnableScheduling
public class Scheduler extends AbstractService{

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    void checkExpiredDiscounts(){
        System.out.println("Discount deletion started");
        List<Discount> discounts = discountRepository.findAll();
        discounts.forEach(d -> {
            if (d.getExpireDate().toLocalDateTime().isBefore(LocalDateTime.now())){
                List<Product> products = productRepository.findAllByDiscount(d);
                products.forEach(p -> {
                    p.setDiscountedPrice(null);
                    p.setDiscount(null);
                    productRepository.save(p);
                } );
                discountRepository.delete(d);
                System.out.println("Discount " + d + "deleted!");
            }
        });

    }

}
