package com.emag.controller;


import com.emag.exception.UnauthorizedException;
import com.emag.model.dto.category.EditCategoryDTO;
import com.emag.model.dto.category.CategoryWithoutIdDTO;
import com.emag.model.dto.review.DoReviewDTO;
import com.emag.model.dto.review.ReviewDTO;
import com.emag.model.pojo.Category;
import com.emag.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
public class ReviewController {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/review/{id}")
    public ResponseEntity<ReviewDTO> addReview (@RequestBody @Valid DoReviewDTO r, @PathVariable long id, HttpServletRequest request){
        return ResponseEntity.ok(reviewService.addReview(r, id,sessionManager.getLoggedUser(request).getId()));
    }

    @PutMapping("/review/{id}")
    public ResponseEntity<ReviewDTO> editReview (@RequestBody DoReviewDTO r, @PathVariable long id, HttpServletRequest request){
        return ResponseEntity.ok(reviewService.editReview(r, id,sessionManager.getLoggedUser(request).getId()));
    }

    @GetMapping("/product/{id}/reviews")
    public List<ReviewDTO> getAllReviewsForProduct(@PathVariable long id){
        return reviewService.getAllReviewsForProduct(id);
    }

    @DeleteMapping("/review/{id}")
    public void deleteReview(@PathVariable long id, HttpServletRequest request){
        if (!sessionManager.userHasPrivileges(request)){
            throw new UnauthorizedException("No permission!");
        }
        reviewService.deleteReview(id);
    }

    @PostMapping("/review/{id}/like")
    public ResponseEntity<ReviewDTO> likeReview (@PathVariable long id, HttpServletRequest request){
        return ResponseEntity.ok(reviewService.likeReview(id, sessionManager.getLoggedUser(request).getId()));
    }

    @GetMapping("/users/{id}/review")
    public List<ReviewDTO> getReviewsByUser(@PathVariable long id){
        return reviewService.getReviewsByUser(id);
    }

}
