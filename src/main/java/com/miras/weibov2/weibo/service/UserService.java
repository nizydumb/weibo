package com.miras.weibov2.weibo.service;

import com.miras.weibov2.weibo.dto.ProfileEditDto;
import com.miras.weibov2.weibo.dto.UserResponseDto;
import com.miras.weibov2.weibo.dto.UserMetaData;
import com.miras.weibov2.weibo.dto.UserProjection;
import com.miras.weibov2.weibo.entity.Post;
import com.miras.weibov2.weibo.entity.User;
import com.miras.weibov2.weibo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final AuthService authService;
    private final StorageService storageService;



    @Value("${logo.upload-dir}")
    String logoFolder;

    
    
    


    //half-auth
    public UserResponseDto getUser(long id)  {
        UserResponseDto userResponseDto = new UserResponseDto();
        UserProjection userProjection = userRepository.findUserById(id);
        if(userProjection == null) return null;
        userResponseDto.setUserProjection(userProjection);
        if (authService.isAuthenticated()) {
            UserMetaData userMetaData = new UserMetaData(isUserFollowedByUser(id, authService.getAuthenticatedUserId()));
            userResponseDto.setUserMetaData(userMetaData);
        }
        return userResponseDto;
    }

    //half-auth
    public Resource loadLogoImage(long id) {
        return storageService.load(id, 1, logoFolder);
    }


//    //full-auth
//    @PreAuthorize("isAuthenticated()")
//    public void uploadLogoImage(MultipartFile image) {
//        storageService.deleteAll(getCurrentUserId(), logoFolder);
//        storageService.save(image, getCurrentUserId(), logoFolder);
//    }

    //full-authentication
    @PreAuthorize("isAuthenticated()")
    public void editUserProfile(ProfileEditDto profileEditDto, MultipartFile file) {
        long id = authService.getAuthenticatedUserId();
        if (userRepository.existsByUsernameAndIdIsNot(profileEditDto.getUsername(), id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not unique username: " + profileEditDto.getUsername());
        storageService.deleteAll(id, logoFolder);
        storageService.save(file, id, logoFolder);
        userRepository.editUser(profileEditDto.getUsername(), profileEditDto.getBio(), profileEditDto.getWebsite(), profileEditDto.getName(), id);
    }







    public boolean isUserFollowedByUser(long firstUserId, long secondUserId) {
        User user = new User();
        user.setId(secondUserId);
        return userRepository.existsUserByIdAndFollowersContaining(firstUserId, user);

    }

    public List<Long> getFollowingUsersIds(long userId) {
        User user = new User();
        user.setId(userId);
        return userRepository.findAllByFollowersContaining(user).stream()
                .map(followings -> followings.getId())
                .collect(Collectors.toList());
    }



}

