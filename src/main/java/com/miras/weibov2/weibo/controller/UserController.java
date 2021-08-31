package com.miras.weibov2.weibo.controller;


import com.miras.weibov2.weibo.dto.ProfileEditDto;
import com.miras.weibov2.weibo.dto.UserResponseDto;
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

    @GetMapping("/{id}")
    ResponseEntity getUser(@PathVariable long id){
        UserResponseDto userResponseDto = userService.getUser(id);
        if(userResponseDto == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/logo/{userId}")
    public ResponseEntity<Resource> loadLogoImage(@PathVariable long userId) {
        Resource resource = userService.loadLogoImage(userId);
        if (resource == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

//    @PostMapping("/logo")
//    public ResponseEntity uploadLogo(@RequestParam("image") MultipartFile image){
//        if(image.isEmpty()) return ResponseEntity.badRequest().build();
//        userService.uploadLogoImage(image);
//        return ResponseEntity.ok().build();
//    }

    @PutMapping("/editProfile")
    public ResponseEntity editProfile(@RequestParam("image") MultipartFile file, @RequestParam("name") String name, @RequestParam("bio") String bio, @RequestParam("website") String website, @RequestParam("username") String username){
        userService.editUserProfile(new ProfileEditDto(name, website, bio, username), file);
        return ResponseEntity.ok().build();

    }












}
