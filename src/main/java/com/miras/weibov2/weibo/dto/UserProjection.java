package com.miras.weibov2.weibo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Value;


public interface UserProjection {

    long getId();
    String getUsername();

    @Value("#{target.followers.size()}")
    int numberOfFollowers();

    @Value("#{target.followings.size()}")
    int numberOfFollowings();

    String getBio();

    String getWebsite();

    String getFullName();





}
