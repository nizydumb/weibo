package com.miras.weibov2.weibo.service;

import com.miras.weibov2.weibo.dto.UserProjection;
import com.miras.weibov2.weibo.entity.User;
import com.miras.weibov2.weibo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;


    @PreAuthorize("isAuthenticated()")
    public long getCurrentUserId() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PreAuthorize("isAuthenticated()")
    public List<Long> getFollowingsIds(long userId) {
        User user = new User();
        user.setId(userId);
        return userRepository.findAllByFollowersContaining(user).stream()
                .map(followings -> Long.valueOf(followings.getId()))
                .collect(Collectors.toList());
    }

}
