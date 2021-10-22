package com.miras.weibov2.weibo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChange {
    String newPassword;
    String oldPassword;
}
