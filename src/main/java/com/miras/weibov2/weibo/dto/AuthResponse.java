package com.miras.weibov2.weibo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    String accessToken;
    String refreshToken;
}
