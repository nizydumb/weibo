package com.miras.weibov2.weibo.dto;


import lombok.Data;

import java.util.Date;

@Data
public class Token {
   String id;
   Date expiresAt;
}
