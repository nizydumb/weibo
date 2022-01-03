package com.miras.weibov2.weibo.repository;

import com.miras.weibov2.weibo.dto.Post;
import com.miras.weibov2.weibo.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<com.miras.weibov2.weibo.entity.User, Long> {



    Optional<com.miras.weibov2.weibo.entity.User> findByUsername(String username);


    Optional<com.miras.weibov2.weibo.entity.User> findByEmail(String email);


    boolean existsUserByUsername(String username);


    boolean existsUserByEmail(String email);


    List<User> findAllByFollowersContaining(com.miras.weibov2.weibo.entity.User user);



    User findUserById(long id);


    boolean existsUserByIdAndFollowersContaining(long firstId, com.miras.weibov2.weibo.entity.User user);


    boolean existsUserByIdAndMyPostsContaining(long userId, Post post);


    @Modifying
    @Query("update User user set user.bio = :bio, user.fullName = :name, user.website = :website, user.username = :username where user.id = :id")
    void editUser(@Param("username") String username, @Param("bio") String bio,@Param("website") String website,@Param("name") String name,@Param("id") long id);


    boolean existsByUsernameAndIdIsNot(String username, long id);

    @Modifying
    @Query("update User user set user.password = :password where user.id = :id")
    void updatePassword(@Param("id") Long id, @Param("password") String encodedPassword);


    List<User> findAllByUsernameContainsOrFullNameContains(String username, String fullName);




    List<User> findAllByFollowingsContaining(com.miras.weibov2.weibo.entity.User user);

}
