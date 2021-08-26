package com.miras.weibov2.weibo.repository;

import com.miras.weibov2.weibo.dto.TokenPairId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Date;


@Repository
@DependsOn("redisTemplate")
public class TokenPairIdRepository {

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    ValueOperations valueOperations;

    @PostConstruct
    public void init(){
        valueOperations = redisTemplate.opsForValue();
    }

    public void save(TokenPairId tokenPairId){
        Date currentDate = new Date();
        valueOperations.set(tokenPairId.getId(), "", Duration.ofMillis(tokenPairId.getExpiresAt().getTime()- currentDate.getTime()));
    }

    public boolean isPresent(TokenPairId tokenPairId){
       return redisTemplate.hasKey(tokenPairId.getId());
    }
}
