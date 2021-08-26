package com.miras.weibov2.weibo.controller;

import com.miras.weibov2.weibo.dto.PostLoadDto;
import com.miras.weibov2.weibo.dto.PostProjection;
import com.miras.weibov2.weibo.repository.PostRepository;
import com.miras.weibov2.weibo.service.PostService;
import com.miras.weibov2.weibo.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final StorageService storageService;
    private final PostService postService;

    @PostMapping("/upload")
    public ResponseEntity uploadPost(@RequestParam("images") MultipartFile[] images,@RequestParam("description") String description){
        for (MultipartFile image: images) {
            if(image.isEmpty()) return ResponseEntity.badRequest().build();
        }
        postService.uploadPost(images, description);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostLoadDto> loadPost(@PathVariable long postId){
        PostLoadDto postLoadDto = postService.loadPostDto(postId);
        if (postLoadDto == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(postLoadDto, HttpStatus.OK);
    }

    @GetMapping("/{postId}/{imageId}")
    public ResponseEntity<Resource> loadImage(@PathVariable long postId, @PathVariable long imageId) {
        Resource resource = storageService.load(postId, imageId);
        if (resource == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    @GetMapping("/profile")
    public ResponseEntity loadProfilePosts(@RequestParam long id, @RequestParam(defaultValue = "0" ) int page){
        List<PostLoadDto> profilePosts = postService.loadPostDtoProfile(id, page);
        if(profilePosts.isEmpty())
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        else
            return ResponseEntity.ok(profilePosts);
    }
    @GetMapping("/feed")
    public ResponseEntity loadFeedPosts(@RequestParam(defaultValue = "0" ) int page){
        List<PostLoadDto> feedPhotos = postService.loadPostDtoFeed(page);
        if(feedPhotos.isEmpty())
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        else
            return ResponseEntity.ok(feedPhotos);
    }

}
