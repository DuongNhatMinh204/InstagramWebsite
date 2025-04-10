package com.nminh.websiteinstagram.repository;

import com.nminh.websiteinstagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String Phone);
    boolean existsByPhone(String phone);
}
