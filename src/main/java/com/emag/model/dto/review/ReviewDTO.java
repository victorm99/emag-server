package com.emag.model.dto.review;

import com.emag.model.dto.user.UserWithoutPasswordDTO;
import com.emag.model.pojo.Product;
import com.emag.model.pojo.Review;
import com.emag.model.pojo.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewDTO {
    @JsonIgnore
    private long id;
    @JsonIgnore
    private UserWithoutPasswordDTO reviewer;
    @JsonIgnore
    private List<User> usersLikedReview;
    private Product product;
    private String title;
    private String description;
    private int rating;
    private LocalDateTime createdAt;
}
