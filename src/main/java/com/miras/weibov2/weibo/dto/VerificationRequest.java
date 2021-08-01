package com.miras.weibov2.weibo.dto;

import lombok.Data;

@Data
public class VerificationRequest {
    String verificationCode;
    String email;
}
