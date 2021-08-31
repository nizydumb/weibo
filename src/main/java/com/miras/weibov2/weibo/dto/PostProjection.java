package com.miras.weibov2.weibo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.miras.weibov2.weibo.repository.LikedPostsRepository;
import com.miras.weibov2.weibo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;


public interface PostProjection {

   // String getDescription();
    Date getCreated();
    long getId();
    int getNumberOfImages();

    @Value("#{target.likes.size()}")
    int getNumberOfLikes();

    @Value("#{target.comments.size()}")
    int getNumberOfComments();

    @Value("#{target.user.getUsername()}")
    String getUsername();

    @Value("#{target.user.getId()}")
    String getUserId();

    @Value("#{target.getDescription()}")
    String getCaption();



}
