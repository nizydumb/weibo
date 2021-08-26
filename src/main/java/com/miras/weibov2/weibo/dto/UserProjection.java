package com.miras.weibov2.weibo.dto;

import org.springframework.beans.factory.annotation.Value;

public interface UserProjection {

    long getId();
    String getUsername();

    @Value("#{target.followers.size()}")
    int numberOfFollowers();

    @Value("#{target.followings.size()}")
    int numberOfFollowings();



}
