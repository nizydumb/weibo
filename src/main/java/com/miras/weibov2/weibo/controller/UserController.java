package com.miras.weibov2.weibo.controller;


import com.miras.weibov2.weibo.dto.TokenPairId;
import com.miras.weibov2.weibo.entity.User;
import com.miras.weibov2.weibo.repository.TokenPairIdRepository;
import com.miras.weibov2.weibo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenPairIdRepository tokenPairIdRepository;







}
