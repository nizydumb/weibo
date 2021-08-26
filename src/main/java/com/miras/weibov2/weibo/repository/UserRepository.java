package com.miras.weibov2.weibo.repository;

import com.miras.weibov2.weibo.dto.PostProjection;
import com.miras.weibov2.weibo.dto.UserProjection;
import com.miras.weibov2.weibo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
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

    List<UserProjection> findAllByFollowersContaining(User user);






}
