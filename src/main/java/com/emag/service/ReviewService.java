package com.emag.service;

import com.emag.exception.BadRequestException;
import com.emag.exception.NotFoundException;
import com.emag.model.dto.review.DoReviewDTO;
import com.emag.model.dto.review.ReviewDTO;
import com.emag.model.pojo.Product;
import com.emag.model.pojo.Review;
import com.emag.model.pojo.User;
import com.emag.util.ReviewUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService extends AbstractService{

    public ReviewDTO addReview(DoReviewDTO r, long productId,long userId) {
        ReviewUtil.validateReview(r);
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product with this id doesn't exist"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with this id doesn't exist"));
        if (reviewRepository.findReviewByReviewerAndProduct(user, product) != null){
            throw new BadRequestException("You have already reviewed this product");
        }
        Review review = modelMapper.map(r, Review.class);
        review.setId(0);
        review.setReviewer(user);
        review.setProduct(product);
        review.setCreatedAt(LocalDateTime.now());
        review = reviewRepository.save(review);
        List<Review> reviews = product.getReviews();
        if (reviews.isEmpty()){
            product.setProductRating(review.getRating());
        }else {
            double total = reviews.stream().map(Review::getRating).reduce(Integer::sum).orElse(null);
            double rating = total / reviews.size();
            product.setProductRating(rating);
        }
        productRepository.save(product);
        return modelMapper.map(review, ReviewDTO.class);
    }

    public ReviewDTO editReview(DoReviewDTO r, long productId, long userId) {
        ReviewUtil.validateReview(r);
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product with this id doesn't exist"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with this id doesn't exist"));
        Review review = reviewRepository.findReviewByReviewerAndProduct(user, product);
        if (review == null){
            throw new BadRequestException("You didn't reviewed this product");
        }
        review.setTitle(r.getTitle());
        review.setDescription(r.getDescription());
        review.setRating(r.getRating());
        reviewRepository.save(review);
        return modelMapper.map(review,ReviewDTO.class);
    }

    public List<ReviewDTO> getAllReviewsForProduct(long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product with this id doesn't exist"));
        List<Review> reviews = product.getReviews();
        List<ReviewDTO> reviewDTOS = new ArrayList<>();
        reviews.forEach(review -> reviewDTOS.add(modelMapper.map(review, ReviewDTO.class)));
        return reviewDTOS;
    }

    public void deleteReview(long id) {
        reviewRepository.delete(reviewRepository.findById(id).orElseThrow(() -> new NotFoundException("Review with this id doesn't exist")));
    }

    public ReviewDTO likeReview(long reviewId, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with this id doesn't exist"));
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new NotFoundException("Review with this id doesn't exist"));
        List<Review> likedReview = user.getReviewsLikedByUser();
        if (likedReview.contains(review)){
            throw new BadRequestException("You have already liked this review");
        }
        List<User> likers = review.getUsersLikedReview();
        likers.add(user);
        review.setUsersLikedReview(likers);
        likedReview.add(review);
        user.setReviewsLikedByUser(likedReview);
        reviewRepository.save(review);
        return modelMapper.map(review,ReviewDTO.class);
    }

    public List<ReviewDTO> getReviewsByUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with this id doesn't exist"));
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewDTO> reviewDTOS = new ArrayList<>();
        reviews.forEach(review -> reviewDTOS.add(modelMapper.map(review, ReviewDTO.class)));
        return reviewDTOS;
    }
}
