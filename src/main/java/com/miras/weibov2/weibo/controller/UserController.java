package com.miras.weibov2.weibo.controller;


import com.miras.weibov2.weibo.dto.UserUpgradeDto;
import com.miras.weibov2.weibo.dto.UserResponse;
import com.miras.weibov2.weibo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    ResponseEntity getUser(@RequestParam("userId") long userId){
        UserResponse userResponse = userService.getUser(userId);
        if(userResponse == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/image")
    public ResponseEntity<Resource> loadUserImage(@RequestParam("userId") long userId) {
        Resource resource = userService.loadImage(userId);
        if (resource == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    @PostMapping("/image")
    public ResponseEntity uploadLogo(@RequestParam("image") MultipartFile image) throws Exception{
        if(image.isEmpty()) return ResponseEntity.badRequest().build();
        userService.uploadImage(image);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity updateProfile(@RequestParam("image") MultipartFile file, @RequestParam("name") String name, @RequestParam("bio") String bio, @RequestParam("website") String website, @RequestParam("username") String username) throws Exception{
        userService.updateUserProfile(new UserUpgradeDto(name, website, bio, username), file);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/follow")
    public ResponseEntity followUser(@RequestParam("userId") long userId){
        UserResponse userResponse = userService.followUser(userId);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/unfollow")
    public ResponseEntity unfollowUser(@RequestParam("userId") long userId){
        UserResponse userResponse = userService.unfollowUser(userId);
        return ResponseEntity.ok(userResponse);
    }

}
