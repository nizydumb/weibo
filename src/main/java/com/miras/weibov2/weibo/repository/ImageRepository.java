package com.miras.weibov2.weibo.repository;

import com.miras.weibov2.weibo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findByPostIdAndImageNumber(long postId, long imageId);

    boolean existsByPostIdAndImageNumber(long postId, long imageId);

}
