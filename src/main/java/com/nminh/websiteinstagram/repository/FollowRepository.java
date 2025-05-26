package com.nminh.websiteinstagram.repository;

import com.nminh.websiteinstagram.entity.Follow;
import com.nminh.websiteinstagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    @Query(value = "SELECT COUNT(F) FROM Follow F WHERE F.following.id = :userId")
    int  getTotalFollowers(Long userId) ;

    @Query(value = "SELECT u FROM User u " +
            "WHERE u.id NOT IN :excludedUserIds " +
            "ORDER BY RAND()")
    Follow findByFollowerAndFollowing(User follower, User following);


//    @Query("SELECT f.following.id FROM Follow f WHERE f.follower.id = :followerId")
//    List<Long> findFollowingIdsByFollowerId(@Param("followerId") Long followerId);
//    List<Long> findFollowingIdsByFollowerId(Long followerId);
    @Query(value = "SELECT following_id FROM follows WHERE follower_id = :followerId", nativeQuery = true)
    List<Long> findFollowingIdsByFollowerId(@Param("followerId") Long followerId);
}
