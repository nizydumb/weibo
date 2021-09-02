package com.miras.weibov2.weibo.controller;

import com.miras.weibov2.weibo.dto.CommentRequestDto;
import com.miras.weibov2.weibo.dto.CommentResponseDto;
import com.miras.weibov2.weibo.dto.PostEditDto;
import com.miras.weibov2.weibo.dto.PostResponseDto;
import com.miras.weibov2.weibo.entity.Post;
import com.miras.weibov2.weibo.service.CommentService;
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

import java.util.List;


@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final StorageService storageService;
    private final PostService postService;
    private final CommentService commentService;

    @PostMapping("/upload")
    public ResponseEntity uploadPost(@RequestParam("images") MultipartFile[] images,@RequestParam("caption") String description){
        for (MultipartFile image: images) {
            if(image.isEmpty()) return ResponseEntity.badRequest().build();
        }
        postService.uploadPost(images, description);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> loadPost(@PathVariable long postId){
        PostResponseDto postResponseDto = postService.loadPostDto(postId);
        return new ResponseEntity<>(postResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{postId}/{imageId}")
    public ResponseEntity<Resource> loadPostImage(@PathVariable long postId, @PathVariable long imageId) {
        Resource resource = postService.loadPostImage(postId, imageId);
        if (resource == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    @GetMapping("/profile")
    public ResponseEntity<List<PostResponseDto>> loadProfilePosts(@RequestParam long id, @RequestParam(defaultValue = "0" ) int page){
        List<PostResponseDto> profilePosts = postService.loadPostDtoProfile(id, page);
        if(profilePosts.isEmpty())
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        else
            return ResponseEntity.ok(profilePosts);
    }
    @GetMapping("/feed")
    public ResponseEntity<List<PostResponseDto>> loadFeedPosts(@RequestParam(defaultValue = "0" ) int page){
        List<PostResponseDto> feedPhotos = postService.loadPostDtoFeed(page);
        if(feedPhotos.isEmpty())
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        else
            return ResponseEntity.ok(feedPhotos);
    }

//    @GetMapping("/like/{postId}")
//    public ResponseEntity likePostAndReturnPostLoadDto(@PathVariable long postId){
//        postService.likePost(postId);
//        return ResponseEntity.ok().build();
//    }
    @PostMapping("/like")
    public ResponseEntity likePost(@RequestBody PostEditDto postEditDto){
        return ResponseEntity.ok(postService.likePost(postEditDto.getId()));
    }


    @PostMapping("/unlike")
    public ResponseEntity unlikePost(@RequestBody PostEditDto postEditDto){
        return ResponseEntity.ok(postService.unlikePost(postEditDto.getId()));
    }

    @PutMapping("/edit")
    public ResponseEntity<PostResponseDto> editPost(@RequestBody PostEditDto postEditDto){
        return ResponseEntity.ok(postService.editPost(postEditDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deletePost(@PathVariable long id){
        postService.deletePost(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/comment")
    public ResponseEntity commentPost(@RequestBody CommentRequestDto commentRequestDto){
        CommentResponseDto commentResponseDto = commentService.commentPostAndReturnCommentResponseDto(commentRequestDto);
        return ResponseEntity.ok(commentResponseDto);

    }




}
