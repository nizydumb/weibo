package com.miras.weibov2.weibo.service;

import com.miras.weibov2.weibo.dto.UserUpgradeDto;
import com.miras.weibov2.weibo.dto.UserResponse;
import com.miras.weibov2.weibo.dto.UserMetaData;
import com.miras.weibov2.weibo.dto.User;
import com.miras.weibov2.weibo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final AuthService authService;
    private final FollowRequestService followRequestService;



    @Value("${logo.upload-dir}")
    String logoFolder;

    
    
    


    //half-auth
    public UserResponse getUser(long id)  {
        UserResponse userResponse = new UserResponse();
        User user = userRepository.findUserById(id);
        if(user == null) return null;
        userResponse.setUser(user);
        if (authService.isAuthenticated()) {
            UserMetaData userMetaData = new UserMetaData(isUserFollowedByUser(id, authService.getAuthenticatedUserId()));
            userResponse.setUserMetaData(userMetaData);
        }
        return userResponse;
    }


    public Resource loadImage(long userId) {
        byte[] image = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).getImage();
        return new ByteArrayResource(image);
    }


    //full-auth
    @PreAuthorize("isAuthenticated()")
    public void uploadImage(MultipartFile image) throws IOException {
        com.miras.weibov2.weibo.entity.User user = userRepository.findById(authService.getAuthenticatedUserId()).get();
        user.setImage(image.getBytes());
        userRepository.save(user);
    }

    //full-authentication
    @PreAuthorize("isAuthenticated()")
    public void updateUserProfile(UserUpgradeDto userUpgradeDto, MultipartFile file) throws IOException {
        long id = authService.getAuthenticatedUserId();
        if (userRepository.existsByUsernameAndIdIsNot(userUpgradeDto.getUsername(), id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not unique username: " + userUpgradeDto.getUsername());
        //userRepository.editUser(userUpgradeDto.getUsername(), userUpgradeDto.getBio(), userUpgradeDto.getWebsite(), userUpgradeDto.getName(), id);
        com.miras.weibov2.weibo.entity.User user = userRepository.findById(id).get();
        user.setUsername(userUpgradeDto.getUsername());
        user.setBio(userUpgradeDto.getBio());
        user.setWebsite(userUpgradeDto.getWebsite());
        user.setFullName(userUpgradeDto.getName());
        user.setImage(file.getBytes());
        userRepository.save(user);
    }

    @PreAuthorize("isAuthenticated()")
    public UserResponse followUser(long userId) {
        long authenticatedUserId = authService.getAuthenticatedUserId();
        followRequestService.followUser(userId, authenticatedUserId);
        return getUser(userId);
    }
    @PreAuthorize("isAuthenticated()")
    public UserResponse unfollowUser(long userId) {
        long authenticatedUserId = authService.getAuthenticatedUserId();
        followRequestService.unfollowUser(userId, authenticatedUserId);
        return getUser(userId);
    }

    public com.miras.weibov2.weibo.entity.User getUserById(long id) {
        return userRepository.findById(id).get();
    }
    public boolean isUserFollowedByUser(long firstUserId, long secondUserId) {
        com.miras.weibov2.weibo.entity.User user = new com.miras.weibov2.weibo.entity.User();
        user.setId(secondUserId);
        return userRepository.existsUserByIdAndFollowersContaining(firstUserId, user);

    }

    public List<Long> getFollowingUsersIds(long userId) {
        com.miras.weibov2.weibo.entity.User user = new com.miras.weibov2.weibo.entity.User();
        user.setId(userId);
        return userRepository.findAllByFollowersContaining(user).stream()
                .map(followings -> followings.getId())
                .collect(Collectors.toList());
    }
}

