package com.miras.weibov2.weibo.service;

import com.miras.weibov2.weibo.dto.PostEditDto;
import com.miras.weibov2.weibo.dto.PostResponseDto;
import com.miras.weibov2.weibo.dto.PostProjection;
import com.miras.weibov2.weibo.dto.PostMetaData;
import com.miras.weibov2.weibo.entity.Post;
import com.miras.weibov2.weibo.entity.User;
import com.miras.weibov2.weibo.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final StorageService storageService;
    private final UserService userService;
    private final LikeService likeService;
    private final AuthService authService;


    @Value("${post.upload-dir}")
    String postFolder;




    @PreAuthorize("isAuthenticated()")
    public PostResponseDto uploadPost(MultipartFile[] images, String description) {
        User currentUser = new User();
        currentUser.setId(authService.getAuthenticatedUserId());
        Post post = Post.builder()
                .description(description)
                .numberOfImages(images.length)
                .user(currentUser)
                .build();
        postRepository.saveAndFlush(post);
        try {
            for (MultipartFile image : images) {
                storageService.save(image, post.getId(), postFolder);
            }
        } catch(Exception e) {
            postRepository.deleteById(post.getId());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return loadPostDto(post.getId());


    }

    //half-auth
    public PostResponseDto loadPostDto(long postId) {
        PostProjection postProjection = postRepository.findPostById(postId);
        if(postProjection == null ) throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        PostMetaData postMetaData = null;
        if(authService.isAuthenticated())
           postMetaData = new PostMetaData(likeService.isPostLikedByUser(authService.getAuthenticatedUserId(), postProjection.getId()));
        return new PostResponseDto(postProjection, postMetaData);
    }

    //half-auth
    public List<PostResponseDto> loadPostDtoProfile(long userId, int pageNumber){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return loadPostDtoProfileByUserId(userId, pageNumber).map(postDtoProfile ->
        {
               if(authService.isAuthenticated()) return new PostResponseDto(postDtoProfile,
                       new PostMetaData(likeService.isPostLikedByUser(authService.getAuthenticatedUserId(), postDtoProfile.getId())));
               else return new PostResponseDto(postDtoProfile, null);
        }
        ).collect(Collectors.toList());
    }

    //auth
    @PreAuthorize("isAuthenticated()")
    public List<PostResponseDto> loadPostDtoFeed(int pageNumber){
        long userId = authService.getAuthenticatedUserId();
        return loadPostDtoFeedByUserId(userService.getFollowingUsersIds(userId), pageNumber)
                .map(postDto -> new PostResponseDto(postDto, new PostMetaData(likeService.isPostLikedByUser(userId, postDto.getId())) ))
                .collect(Collectors.toList());
    }


    //auth
    @PreAuthorize("isAuthenticated()")
    public PostResponseDto likePost(long postId) {
        long userId = authService.getAuthenticatedUserId();
        if(!postRepository.existsById(postId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        likeService.likePostByUser(postId, userId);
        return new PostResponseDto(postRepository.findPostById(postId), new PostMetaData(likeService.isPostLikedByUser(userId, postId)));

    }

    //authentication
    @PreAuthorize("isAuthenticated()")
    public PostResponseDto unlikePost(long postId) {
        long userId = authService.getAuthenticatedUserId();
        if(!postRepository.existsById(postId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        likeService.deleteLikeByUserIdAndPostId(postId, userId);
        return new PostResponseDto(postRepository.findPostById(postId), new PostMetaData(likeService.isPostLikedByUser(userId, postId)));

    }


    //full-autorization
    @PreAuthorize("isAuthenticated() and @postService.existsUserByPost(@userService.currentUserId, #postEditDto.id)")
    public PostResponseDto editPost(PostEditDto postEditDto) {
        // initial method first loads Post object from db and saves it causing performance issue
//        Post post = postRepository.findById(postEditDto.getId()).get();
//        post.setId(postEditDto.getId());
//        post.setDescription(postEditDto.getDescription());
//        postRepository.save(post);
        postRepository.update(postEditDto.getId(), postEditDto.getDescription());
        return loadPostDto(postEditDto.getId());


    }

    //full-authorization
    @PreAuthorize("isAuthenticated() and @postService.existsUserByPost(@userService.currentUserId, #postId)")
    public void deletePost(long postId){
        postRepository.deleteById(postId);
    }




    //no-auth
    public Resource loadPostImage(long postId, long imageId) {
        return storageService.load(postId, imageId, postFolder);
    }


    private boolean existsUserByUserIdAndPostId(long userId, long postId){
        return postRepository.existsByIdAndUserId(postId, userId);
    }

    private Stream<PostProjection> loadPostDtoProfileByUserId(long userId, int pageNumber) {
        User user = new User();
        user.setId(userId);
        return postRepository.findAllByUserOrderByCreatedDesc(user, PageRequest.of(pageNumber,20)).stream();
    }

    private Stream<PostProjection> loadPostDtoFeedByUserId(List<Long> followingIds, int pageNumber) {
        return postRepository.findAllByUserIdInOrderByCreatedDesc(followingIds, PageRequest.of(pageNumber, 20)).stream();

    }
}
