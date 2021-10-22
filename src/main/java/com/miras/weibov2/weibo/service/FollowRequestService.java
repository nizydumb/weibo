package com.miras.weibov2.weibo.service;

import com.miras.weibov2.weibo.entity.FollowRequest;
import com.miras.weibov2.weibo.entity.User;
import com.miras.weibov2.weibo.repository.FollowRequestRepository;
import com.miras.weibov2.weibo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowRequestService {
    private final FollowRequestRepository followRequestRepository;
    private final UserRepository userService;

    public void followUser(long userId, long authenticatedUserId) {
        FollowRequest followRequest = new FollowRequest();
        Optional<User> userOptional = userService.findById(userId);
        if(!userOptional.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with id: " + userId+ " not exists");
        if(followRequestRepository.existsByFollowerIdAndFollowedId(authenticatedUserId, userId)) return;
        User user = userOptional.get();
        User follower = userService.findById(authenticatedUserId).get();
        followRequest.setFollowed(user);
        followRequest.setFollower(follower);
        followRequestRepository.save(followRequest);

    }
    public void unfollowUser(long userId, long authenticatedUserId) {
        if(followRequestRepository.existsByFollowerIdAndFollowedId(authenticatedUserId, userId)){
            FollowRequest followRequest = followRequestRepository.findByFollowerIdAndFollowedId(authenticatedUserId, userId);
            followRequestRepository.deleteById(followRequest.getId());
        }

    }
}
