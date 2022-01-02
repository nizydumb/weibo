package com.miras.weibov2.weibo.service;

import com.miras.weibov2.weibo.dto.*;
import com.miras.weibov2.weibo.entity.Post;
import com.miras.weibov2.weibo.entity.User;
import com.miras.weibov2.weibo.repository.CommentRepository;
import com.miras.weibov2.weibo.repository.PostRepository;
import com.miras.weibov2.weibo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final LikeService likeService;
    private final AuthService authService;
    private final PostService postService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;



    @PreAuthorize("isAuthenticated()")
    public CommentResponse commentPost(CommentRequest commentRequest) {
        com.miras.weibov2.weibo.entity.Comment comment = new com.miras.weibov2.weibo.entity.Comment();
        Optional<Post> postOptional = postRepository.findById(commentRequest.getPostId());
        if(!postOptional.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post with id " + commentRequest.getPostId() + " not exists");
        Post post = postOptional.get();
        User author = userRepository.findById(authService.getAuthenticatedUserId()).get();
        if(!(commentRequest.getCommentIdRepliedTo() == 0)) {
            com.miras.weibov2.weibo.entity.Comment repliedTo = commentRepository.findById(commentRequest.getCommentIdRepliedTo()).get();
            repliedTo.setId(commentRequest.getCommentIdRepliedTo());
            comment.setRepliedToComment(repliedTo);
            com.miras.weibov2.weibo.entity.Comment parentComment= commentRepository.findCommentByChildrenCommentsContaining(comment.getRepliedToComment());
            if(!(parentComment == null)){
                comment.setParentComment(parentComment);
            }
            else comment.setParentComment(repliedTo);
        }
        comment.setContent(commentRequest.getContent());
        comment.setAuthor(author);
        comment.setPost(post);
        commentRepository.saveAndFlush(comment);
        return getComment(comment.getId());
    }


    public List<CommentResponse> loadCommentsByPostId(long postId, int pageNumber) {

        if(!postService.existsPostById(postId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post with id " + postId + "is not found");
         Page<Comment> commentProjections = commentRepository.findByPostIdAndParentCommentIsNullOrderByNumberOfLikesDescCreatedDesc(postId, PageRequest.of(pageNumber, 50));
         List<CommentResponse> commentResponse = commentProjections.stream()
                 .map(comment ->
                         { if(authService.isAuthenticated()) return new CommentResponse(comment,
                new CommentMetaData(likeService.isCommentLikedByUser(authService.getAuthenticatedUserId(), comment.getId())));
        else return new CommentResponse(comment, null);
        }
        ).collect(Collectors.toList());
         return commentResponse;

    }

    public List<CommentResponse> getChildrenOfComment(long commentId, int pageNumber) {
        if(!commentRepository.existsById(commentId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment with id " + commentId + " not exists");
        Page<Comment> commentProjections = commentRepository.findByParentCommentIdOrderByCreatedAsc(commentId, PageRequest.of(pageNumber, 50 ));
        List<CommentResponse> commentResponse = commentProjections.stream()
                .map(comment ->
                        { if(authService.isAuthenticated()) return new CommentResponse(comment,
                                new CommentMetaData(likeService.isCommentLikedByUser(authService.getAuthenticatedUserId(), comment.getId())));
                        else return new CommentResponse(comment, null);
                        }
                ).collect(Collectors.toList());
        return commentResponse;
    }

    @PreAuthorize("isAuthenticated()")
    public CommentResponse likeComment(long commentID) {
        if(!commentRepository.existsById(commentID)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment with id " + commentID + " not exists");
        long userID = authService.getAuthenticatedUserId();
        likeService.likeCommentByUser(commentID, userID);
        return new CommentResponse(commentRepository.findCommentById(commentID), new CommentMetaData(likeService.isCommentLikedByUser(userID, commentID)) );

    }

    @PreAuthorize("isAuthenticated()")
    public CommentResponse unlikeComment(long commentID) {
        if(!commentRepository.existsById(commentID)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment with id " + commentID + " not exists");
        long userId = authService.getAuthenticatedUserId();
        likeService.deleteLikeByUserIdAndCommentId(userId, commentID);
        return new CommentResponse(commentRepository.findCommentById(commentID), new CommentMetaData(likeService.isCommentLikedByUser(userId, commentID)) );
    }

    @PreAuthorize("isAuthenticated() and @commentService.existsUserByComment(@authService.authenticatedUserId, #commentId)")
    public CommentResponse updateComment(long commentId, String body) {
        long userId = authService.getAuthenticatedUserId();
        if(!commentRepository.existsById(commentId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment with id " + commentId + " not exists");
        commentRepository.update(commentId, body);
        return new CommentResponse(commentRepository.findCommentById(commentId), new CommentMetaData(likeService.isCommentLikedByUser(userId, commentId)) );
    }

    @PreAuthorize("isAuthenticated() and @commentService.existsUserByComment(@authService.authenticatedUserId, #commentID)")
    public void deleteComment(long commentID) {
       commentRepository.deleteById(commentID);
    }

    public boolean existsUserByComment(long userID, long commentID){
        return commentRepository.existsByAuthorIdAndId(userID, commentID);
    }

    public CommentResponse getComment(long commentID){
        if(!commentRepository.existsById(commentID)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Comment with id: " + commentID + " not exists");
        Comment comment = commentRepository.findCommentById(commentID);
        CommentMetaData commentMetaData = null;
        if(authService.isAuthenticated()) {
            commentMetaData = new CommentMetaData(likeService.isCommentLikedByUser(authService.getAuthenticatedUserId(), commentID));
        }
        return new CommentResponse(comment, commentMetaData);

    }
}
