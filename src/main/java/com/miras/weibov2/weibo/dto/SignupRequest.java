package com.miras.weibov2.weibo.dto;

import lombok.Data;

@Data
public class SignupRequest {
    String username;
    String password;
    String email;
}
