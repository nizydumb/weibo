package com.miras.weibov2.weibo.repository;

import com.miras.weibov2.weibo.entity.LikedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
@Transactional
public interface LikedPostsRepository extends JpaRepository<LikedPost, Long> {

    public boolean existsLikedPostByUserIdAndPostId(long userId, long postId);
    
    public void deleteByUserIdAndPostId(long userId, long postId);





}
