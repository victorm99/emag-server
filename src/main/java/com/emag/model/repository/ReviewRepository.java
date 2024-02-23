package com.emag.model.repository;

import com.emag.model.pojo.Product;
import com.emag.model.pojo.Review;
import com.emag.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findReviewByReviewerAndProduct(User u, Product p);
}
