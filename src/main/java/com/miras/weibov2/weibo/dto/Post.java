package com.miras.weibov2.weibo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.miras.weibov2.weibo.repository.LikedPostsRepository;
import com.miras.weibov2.weibo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;


public interface Post {


    Date getCreated();
    long getId();

    @Value("#{target.images == null ? 0 : target.images.size()}")
    int getNumberOfImages();

    @Value("#{target.likes == null ? 0 : target.likes.size()}")
    int getNumberOfLikes();

    @Value("#{target.comments == null ? 0 : target.comments.size()}")
    int getNumberOfComments();

    UserProjection getUser();

    @Value("#{target.getDescription() == null ? '' : target.getDescription()}")
    String getCaption();


    interface UserProjection{
        long getId();
        String getUsername();
    }
}
