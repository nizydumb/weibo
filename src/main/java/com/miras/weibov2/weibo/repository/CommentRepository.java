package com.miras.weibov2.weibo.repository;

import com.miras.weibov2.weibo.dto.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CommentRepository extends JpaRepository<com.miras.weibov2.weibo.entity.Comment, Long> {


    com.miras.weibov2.weibo.entity.Comment findCommentByChildrenCommentsContaining(com.miras.weibov2.weibo.entity.Comment comment);


    Comment findCommentById(Long id);

    Page<Comment> findByPostIdAndParentCommentIsNullOrderByNumberOfLikesDescCreatedDesc(long postId, Pageable page);

    Page<Comment> findByParentCommentIdOrderByCreatedAsc(long commentId, Pageable page);

    boolean existsByAuthorIdAndId(long userId, long commentId);

    @Modifying
    @Query("update Comment comment set comment.content = :body, comment.updated = current_timestamp where comment.id = :id")
    void update(long id,@Param("body") String body);
}