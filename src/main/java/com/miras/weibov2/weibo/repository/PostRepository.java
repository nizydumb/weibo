package com.miras.weibov2.weibo.repository;


import com.miras.weibov2.weibo.dto.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface PostRepository extends JpaRepository<com.miras.weibov2.weibo.entity.Post, Long> {


    public Post findPostById(long id);

    public Page<Post> findAllByUserIdOrderByCreatedDesc(long userId, Pageable pageable);

    public Page<Post> findAllByUserIdInOrderByCreatedDesc(List<Long> ids, Pageable pageable);


    @Modifying
    @Query("update Post post set post.description = :description, post.updated = current_timestamp where post.id = :id")
    public void update(@Param("id") long id,@Param("description") String description);

    public boolean existsByIdAndUserId(long postId, long userId);

    public void deleteById(long postId);
}
