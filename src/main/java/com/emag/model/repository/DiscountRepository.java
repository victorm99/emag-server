package com.emag.model.repository;

import com.emag.model.pojo.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findDiscountByDiscountPercentAndStartDateAndExpireDate(int discountPercent, Timestamp startDate, Timestamp expireDate);
}
