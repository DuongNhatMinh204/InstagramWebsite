package com.nminh.websiteinstagram.repository;

import com.nminh.websiteinstagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String Phone);
    boolean existsByPhone(String phone);

    @Query("SELECT u FROM User u WHERE LOWER(u.nickName) LIKE LOWER(CONCAT('%', :nickName, '%'))")
    List<User> findByNickNameContainingIgnoreCase(@Param("nickName") String nickName);

    @Query(value = "SELECT * FROM users u WHERE u.id NOT IN (:excludedIds) ORDER BY RAND() LIMIT :limit",
            nativeQuery = true)
    List<User> findRandomUsersNotInIds(@Param("excludedIds") List<Long> excludedIds,
                                       @Param("limit") int limit);
}
