package com.miras.weibov2.weibo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserResponse {

    User user;
    UserMetaData userMetaData;


}
