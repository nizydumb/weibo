package com.miras.weibov2.weibo.repository;

import com.miras.weibov2.weibo.entity.LikedComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
@Transactional
public interface LikedCommentsRepository extends JpaRepository<LikedComment, Long> {

    public boolean existsLikedCommentByUserIdAndCommentId(long userId,long commentIdId);

    public void deleteByUserIdAndCommentId(long userId, long commentId);

}
