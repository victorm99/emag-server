package com.emag.model.repository;

import com.emag.model.pojo.Product;
import com.emag.model.pojo.User;
import com.emag.model.pojo.UserCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<UserCart , UserCart.UserCartKey> {
    Optional<UserCart> findByPrimaryKey(UserCart.UserCartKey userCartKey);
    Optional<List<UserCart>> findAllByUser (User user);
}
