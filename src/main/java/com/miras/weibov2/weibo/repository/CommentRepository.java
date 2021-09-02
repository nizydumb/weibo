package com.miras.weibov2.weibo.repository;

import com.miras.weibov2.weibo.dto.CommentProjection;
import com.miras.weibov2.weibo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {
    CommentProjection findCommentByChildrenCommentsContaining(Comment comment);

    CommentProjection findCommentById(Long id);
}
