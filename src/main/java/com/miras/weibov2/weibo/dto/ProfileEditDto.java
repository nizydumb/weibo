package com.miras.weibov2.weibo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileEditDto {

    String name;

    String website;

    String bio;

    String username;

}
