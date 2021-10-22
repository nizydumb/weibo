package com.miras.weibov2.weibo.controller;


import com.miras.weibov2.weibo.dto.*;
import com.miras.weibov2.weibo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;


    @PostMapping()
    public ResponseEntity commentPost(@RequestBody CommentRequest commentRequest){
        CommentResponse commentResponse = commentService.commentPost(commentRequest);
        return ResponseEntity.ok(commentResponse);

    }
    @GetMapping()
    public ResponseEntity getPostComments(@RequestParam long postId, @RequestParam(defaultValue = "0") int page){
        List<CommentResponse> commentResponses = commentService.loadCommentsByPostId(postId, page);
        if(commentResponses.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(commentResponses);
    }

    @GetMapping("/children")
    public ResponseEntity getCommentsChildren(@RequestParam long commentId, @RequestParam(defaultValue = "0") int page){
        List<CommentResponse> commentResponses = commentService.getChildrenOfComment(commentId, page);
        if(commentResponses.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(commentResponses);


    }
    @GetMapping("/like")
    public ResponseEntity likeComment(@RequestParam("commentId") long commentId){
        return ResponseEntity.ok(commentService.likeComment(commentId));
    }


    @GetMapping("/unlike")
    public ResponseEntity unlikeComment(@RequestParam("commentId") long commentId){
        return ResponseEntity.ok(commentService.unlikeComment(commentId));
    }

    @PutMapping
    public ResponseEntity<CommentResponse> updateComment(@RequestParam("commentId") long commentId, @RequestParam("body") String body){
        return ResponseEntity.ok(commentService.updateComment(commentId, body));
    }

    @DeleteMapping
    public ResponseEntity deleteComment(@RequestParam("commentId") long commentId){
        commentService.deleteComment(commentId);
        return new ResponseEntity(HttpStatus.OK);
    }




}
