package com.miras.weibov2.weibo.service;

import com.miras.weibov2.weibo.dto.PostLoadDto;
import com.miras.weibov2.weibo.dto.PostProjection;
import com.miras.weibov2.weibo.dto.UserMetaDataDto;
import com.miras.weibov2.weibo.dto.UserProjection;
import com.miras.weibov2.weibo.entity.LikedPost;
import com.miras.weibov2.weibo.entity.Post;
import com.miras.weibov2.weibo.entity.User;
import com.miras.weibov2.weibo.repository.LikedPostsRepository;
import com.miras.weibov2.weibo.repository.PostRepository;
import com.miras.weibov2.weibo.repository.UserRepository;
import com.miras.weibov2.weibo.service.impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final StorageService storageService;
    private final UserService userService;
    private final LikedPostsService likedPostsService;
    private final PostServiceImpl postServiceImpl;

    public void uploadPost(MultipartFile[] images, String description) {
        User currentUser = new User();
        currentUser.setId(userService.getCurrentUserId());
        Post post = Post.builder()
                .description(description)
                .numberOfImages(images.length)
                .user(currentUser)
                .build();
        postRepository.saveAndFlush(post);
        for(MultipartFile image : images){
            storageService.save(image, post.getId());
        }

    }

    //half-auth
    public PostLoadDto loadPostDto(long postId){
        PostProjection postProjection = postRepository.findPostById(postId);
        UserMetaDataDto userMetaDataDto = null;
       if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
           userMetaDataDto = new UserMetaDataDto(likedPostsService.isPostLikedByUser(userService.getCurrentUserId(), postProjection.getId()));
       return new PostLoadDto(postProjection, userMetaDataDto);
    }

//    public List<PostLoadDto> loadPostDtoProfile(long userId, int pageNumber){
//        User user = new User();
//        user.setId(userId);
//        return postRepository.findAllByUserOrderByCreatedDesc(user, PageRequest.of(pageNumber,20)).stream()
//                .map(postProjection -> new PostLoadDto(postProjection, likedPostsService.isPostLikedByCurrentUser(postProjection.getId()) ))
//                .collect(Collectors.toList());
//       // return postServiceImpl.loadPostDtoProfileByUserId(userId, pageNumber);
//    }

    //half-auth
    public List<PostLoadDto> loadPostDtoProfile(long userId, int pageNumber){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication.isAuthenticated() && (! (authentication instanceof AnonymousAuthenticationToken));
        return postServiceImpl.loadPostDtoProfileByUserId(userId, pageNumber).map(postDtoProfile ->
        {
               if(isAuthenticated) return new PostLoadDto(postDtoProfile,
                       new UserMetaDataDto(likedPostsService.isPostLikedByUser(Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()), postDtoProfile.getId())));
               else return new PostLoadDto(postDtoProfile, null);
        }
        ).collect(Collectors.toList());
    }

    //full-auth
    @PreAuthorize("isAuthenticated()")
    public List<PostLoadDto> loadPostDtoFeed(int pageNumber){
        long userId = userService.getCurrentUserId();
        return postServiceImpl.loadPostDtoFeedByUserId(userService.getFollowingsIds(userId), pageNumber)
                .map(postDto -> new PostLoadDto(postDto, new UserMetaDataDto(likedPostsService.isPostLikedByUser(userId, postDto.getId())) ))
                .collect(Collectors.toList());
    }


}
