package com.miras.weibov2.weibo.service;


import com.miras.weibov2.weibo.dto.PostResponse;
import com.miras.weibov2.weibo.dto.Post;
import com.miras.weibov2.weibo.dto.PostMetaData;
import com.miras.weibov2.weibo.entity.Image;
import com.miras.weibov2.weibo.entity.User;
import com.miras.weibov2.weibo.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final LikeService likeService;
    private final AuthService authService;
    private final ImageService imageService;



    @PreAuthorize("isAuthenticated()")
    public PostResponse uploadPost(MultipartFile[] images, String description) {
        User user = userService.getUserById(authService.getAuthenticatedUserId());
        com.miras.weibov2.weibo.entity.Post post = new com.miras.weibov2.weibo.entity.Post();
        post.setUser(user);
        post.setDescription(description);
        postRepository.saveAndFlush(post);
        long postId = post.getId();
        List<Image> nullImages = new ArrayList<>();
        for(MultipartFile image : images){
            nullImages.add(new Image());
        }
        post.setImages(nullImages);

        try {
            imageService.uploadImage(images, postId);
        } catch(Exception e) {
            postRepository.deleteById(postId);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return loadPostDto(postId);


    }

    //half-auth
    public PostResponse loadPostDto(long postId) {
        Post post = postRepository.findPostById(postId);
        if(post == null ) throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        PostMetaData postMetaData = null;
        if(authService.isAuthenticated())
           postMetaData = new PostMetaData(likeService.isPostLikedByUser(authService.getAuthenticatedUserId(), post.getId()));
        return new PostResponse(post, postMetaData);
    }

    //half-auth
    public List<PostResponse> loadPostDtoProfile(long userId, int pageNumber){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return profilePostsStream(userId, pageNumber).map(postDtoProfile ->
        {
               if(authService.isAuthenticated()) return new PostResponse(postDtoProfile,
                       new PostMetaData(likeService.isPostLikedByUser(authService.getAuthenticatedUserId(), postDtoProfile.getId())));
               else return new PostResponse(postDtoProfile, null);
        }
        ).collect(Collectors.toList());
    }

    //auth
    @PreAuthorize("isAuthenticated()")
    public List<PostResponse> loadPostDtoFeed(int pageNumber){
        long userId = authService.getAuthenticatedUserId();
        return feedPostsStream(userService.getFollowingUsersIds(userId), pageNumber)
                .map(postDto -> new PostResponse(postDto, new PostMetaData(likeService.isPostLikedByUser(userId, postDto.getId())) ))
                .collect(Collectors.toList());
    }


    //auth
    @PreAuthorize("isAuthenticated()")
    public PostResponse likePost(long postId) {
        long userId = authService.getAuthenticatedUserId();
        if(!postRepository.existsById(postId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post with id: " + postId + " not exists");
        likeService.likePostByUser(postId, userId);
        return new PostResponse(postRepository.findPostById(postId), new PostMetaData(likeService.isPostLikedByUser(userId, postId)));

    }

    //authentication
    @PreAuthorize("isAuthenticated()")
    public PostResponse unlikePost(long postId) {
        long userId = authService.getAuthenticatedUserId();
        if(!postRepository.existsById(postId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post with id: " + postId + " not exists");
        likeService.deleteLikeByUserIdAndPostId(postId, userId);
        return new PostResponse(postRepository.findPostById(postId), new PostMetaData(likeService.isPostLikedByUser(userId, postId)));

    }


    //full-autorization
    @PreAuthorize("isAuthenticated() and @postService.existsUserByPost(@authService.authenticatedUserId, #postEditDto.id)")
    public PostResponse editPost(long postId, String description) {
        // initial method first loads Post object from db and saves it causing performance issue
//        Post post = postRepository.findById(postEditDto.getId()).get();
//        post.setId(postEditDto.getId());
//        post.setDescription(postEditDto.getDescription());
//        postRepository.save(post);
        postRepository.update(postId, description);
        return loadPostDto(postId);


    }

    //full-authorization
    @PreAuthorize("isAuthenticated() and @postService.existsUserByPost(@authService.authenticatedUserId, #postId)")
    public void deletePost(long postId){
        postRepository.deleteById(postId);
    }




    //no-auth
    public Resource getImage(long postId, long imageId) {
        return imageService.getImage(postId, imageId);
    }


    private boolean existsUserByUserIdAndPostId(long userId, long postId){
        return postRepository.existsByIdAndUserId(postId, userId);
    }

    private Stream<Post> profilePostsStream(long userId, int pageNumber) {
        return postRepository.findAllByUserIdOrderByCreatedDesc(userId, PageRequest.of(pageNumber,20)).stream();
    }

    private Stream<Post> feedPostsStream(List<Long> followingIds, int pageNumber) {
        return postRepository.findAllByUserIdInOrderByCreatedDesc(followingIds, PageRequest.of(pageNumber, 20)).stream();

    }

    public boolean existsPostById(long postId) {
        return postRepository.existsById(postId);
    }
}
