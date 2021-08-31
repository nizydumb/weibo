package com.miras.weibov2.weibo.repository;

import com.miras.weibov2.weibo.dto.PostProjection;
import com.miras.weibov2.weibo.dto.UserProjection;
import com.miras.weibov2.weibo.entity.Post;
import com.miras.weibov2.weibo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Transactional
    Optional<User> findByUsername(String username);

    @Transactional
    Optional<User> findByEmail(String email);

    @Transactional
    boolean existsUserByUsername(String username);

    @Transactional
    boolean existsUserByEmail(String email);

    @Transactional
    List<UserProjection> findAllByFollowersContaining(User user);


    @Transactional
    UserProjection findUserById(long id);

    @Transactional
    boolean existsUserByIdAndFollowersContaining(long firstId, User user);

    @Transactional
    boolean existsUserByIdAndMyPostsContaining(long userId, Post post);

    @Transactional
    @Modifying
    @Query("update User user set user.bio = :bio, user.fullName = :name, user.website = :website, user.username = :username where user.id = :id")
    void editUser(@Param("username") String username, @Param("bio") String bio,@Param("website") String website,@Param("name") String name,@Param("id") long id);

    @Transactional
    boolean existsByUsernameAndIdIsNot(String username, long id);
}
