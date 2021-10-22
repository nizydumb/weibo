package com.miras.weibov2.weibo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Value;


public interface User {

    long getId();
    String getUsername();

    @Value("#{target.followers.size()}")
    int getNumberOfFollowers();

    @Value("#{target.followings.size()}")
    int getNumberOfFollowings();

    @Value("#{target.myPosts.size()}")
    int getNumberOfPosts();

    String getBio();

    String getWebsite();

    @Value("#{target.fullName}")
    String getName();





}
