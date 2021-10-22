package com.miras.weibov2.weibo.service;

import com.miras.weibov2.weibo.entity.*;
import com.miras.weibov2.weibo.repository.LikedCommentsRepository;
import com.miras.weibov2.weibo.repository.LikedPostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikedPostsRepository likedPostsRepository;
    private final LikedCommentsRepository likedCommentsRepository;

    public boolean isPostLikedByUser(long userId, long postId) {
        return likedPostsRepository.existsLikedPostByUserIdAndPostId(userId, postId);
    }

    public void likePostByUser(long postId, long userId) {
        if(!likedPostsRepository.existsLikedPostByUserIdAndPostId(userId, postId)){
            LikedPost likedPost = new LikedPost();
            Post post = new Post();
            post.setId(postId);
            User user = new User();
            user.setId(userId);
            likedPost.setPost(post);
            likedPost.setUser(user);
            likedPostsRepository.save(likedPost);
        }
    }

    public void deleteLikeByUserIdAndPostId(long postId, long userId) {
        if(likedPostsRepository.existsLikedPostByUserIdAndPostId(userId, postId))
            likedPostsRepository.deleteByUserIdAndPostId(userId, postId);
    }

    public boolean isCommentLikedByUser(long userId, long commentId) {
        return likedCommentsRepository.existsLikedCommentByUserIdAndCommentId(userId, commentId);
    }

    public void likeCommentByUser(long commentId, long userId) {
        if(!likedCommentsRepository.existsLikedCommentByUserIdAndCommentId(userId, commentId)){
            LikedComment likedComment = new LikedComment();
            Comment comment = new Comment();
            comment.setId(commentId);
            User user = new User();
            user.setId(userId);
            likedComment.setComment(comment);
            likedComment.setUser(user);
            likedCommentsRepository.save(likedComment);
        }
    }

    public void deleteLikeByUserIdAndCommentId(long userId, long commentId) {
        if(likedCommentsRepository.existsLikedCommentByUserIdAndCommentId(userId, commentId))
            likedCommentsRepository.deleteByUserIdAndCommentId(userId, commentId);
    }

}
