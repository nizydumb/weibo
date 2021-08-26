package com.miras.weibov2.weibo.repository;

import com.miras.weibov2.weibo.dto.PostProjection;
import com.miras.weibov2.weibo.entity.Post;
import com.miras.weibov2.weibo.entity.User;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {

    public PostProjection findPostById(long id);

    public Page<PostProjection> findAllByUserOrderByCreatedDesc(User user, Pageable pageable);

    public Page<PostProjection> findAllByUserIdInOrderByCreatedDesc(List<Long> ids, Pageable pageable);





}
