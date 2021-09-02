package com.miras.weibov2.weibo.repository;

import com.miras.weibov2.weibo.dto.Token;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TokenRepository {


    private final RedisTemplate<String, String> redisTemplate;

    private ValueOperations valueOperations;

    @PostConstruct
    public void init(){
        valueOperations = redisTemplate.opsForValue();
    }

    public void save(Token token){
        Date currentDate = new Date();
        valueOperations.set(token.getId(), "", Duration.ofMillis(token.getExpiresAt().getTime()- currentDate.getTime()));
    }

    public boolean isPresent(Token token){
       return redisTemplate.hasKey(token.getId());
    }
}
