package com.miras.weibov2.weibo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Calendar;
import java.util.Date;

@Data
public class TokenPairId {
   String id;
   Date expiresAt;
}
