package com.miras.weibov2.weibo.controller;

import com.miras.weibov2.weibo.dto.PostResponse;
import com.miras.weibov2.weibo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {


    private final PostService postService;


    @PostMapping
    public ResponseEntity uploadPost(@RequestParam("images") MultipartFile[] images,@RequestParam("caption") String description){
        for (MultipartFile image: images) {
            if(image.isEmpty()) return ResponseEntity.badRequest().build();
        }
        PostResponse postResponse =  postService.uploadPost(images, description);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping
    public ResponseEntity<PostResponse> loadPost(@RequestParam("postId") long postId){
        PostResponse postResponse = postService.loadPostDto(postId);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/image")
    public ResponseEntity<Resource> loadPostImage(@RequestParam("postId") long postId, @RequestParam("imageId") long imageId) {
        Resource resource = postService.getImage(postId, imageId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    @GetMapping("/profile")
    public ResponseEntity<List<PostResponse>> loadProfilePosts(@RequestParam long userId, @RequestParam(defaultValue = "0", value = "page") int page){
        List<PostResponse> profilePosts = postService.loadPostDtoProfile(userId, page);
        if(profilePosts.isEmpty())
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        else
            return ResponseEntity.ok(profilePosts);
    }
    @GetMapping("/feed")
    public ResponseEntity<List<PostResponse>> loadFeedPosts(@RequestParam(defaultValue = "0", value = "page") int page){
        List<PostResponse> feedPhotos = postService.loadPostDtoFeed(page);
        if(feedPhotos.isEmpty())
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        else
            return ResponseEntity.ok(feedPhotos);
    }

    @GetMapping("/like")
    public ResponseEntity likePost(@RequestParam("postId") long postId){
        return ResponseEntity.ok(postService.likePost(postId));
    }


    @GetMapping("/unlike")
    public ResponseEntity unlikePost(@RequestParam("postId") long postId){
        return ResponseEntity.ok(postService.unlikePost(postId));
    }

    @PutMapping
    public ResponseEntity<PostResponse> editPost(@RequestParam("postId") long postId, @RequestParam("caption") String description){
        return ResponseEntity.ok(postService.editPost(postId, description));
    }

    @DeleteMapping
    public ResponseEntity deletePost(@RequestParam("postId") long postId){
        postService.deletePost(postId);
        return new ResponseEntity(HttpStatus.OK);
    }






}
