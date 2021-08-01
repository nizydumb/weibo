package com.miras.weibov2.weibo.controller;


import com.miras.weibov2.weibo.entity.User;
import com.miras.weibov2.weibo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    @Autowired
    UserRepository userRepository;


    @GetMapping("/{username}")
    public User getUser(@PathVariable("username") String username){
        Optional<User> user = userRepository.findByUsername(username);
        User user1 = user.get();
        return user1;
    }

}
