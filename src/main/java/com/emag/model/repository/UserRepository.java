package com.emag.model.repository;

import com.emag.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findAllBySubscribedIsTrue();
    Optional<User> findByMobilePhoneContaining (String mobilePhone);
}
