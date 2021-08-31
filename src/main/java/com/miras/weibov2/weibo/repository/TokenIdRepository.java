package com.miras.weibov2.weibo.repository;

import com.miras.weibov2.weibo.dto.TokenId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Date;


@Repository
@DependsOn("redisTemplate")
public class TokenIdRepository {

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    ValueOperations valueOperations;

    @PostConstruct
    public void init(){
        valueOperations = redisTemplate.opsForValue();
    }

    public void save(TokenId tokenId){
        Date currentDate = new Date();
        valueOperations.set(tokenId.getId(), "", Duration.ofMillis(tokenId.getExpiresAt().getTime()- currentDate.getTime()));
    }

    public boolean isPresent(TokenId tokenId){
       return redisTemplate.hasKey(tokenId.getId());
    }
}
