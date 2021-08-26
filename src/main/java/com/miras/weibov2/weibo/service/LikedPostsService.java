package com.miras.weibov2.weibo.service;

import com.miras.weibov2.weibo.entity.Post;
import com.miras.weibov2.weibo.entity.User;
import com.miras.weibov2.weibo.repository.LikedPostsRepository;
import com.miras.weibov2.weibo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikedPostsService {

    private final LikedPostsRepository likedPostsRepository;
    private final UserService userService;

    public Boolean isPostLikedByCurrentUser(long postId) {
        try {
            User user = new User();
            user.setId(userService.getCurrentUserId());
            Post post = new Post();
            post.setId(postId);
            return likedPostsRepository.existsLikedPostByUserAndPost(user, post);
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean isPostLikedByUser(long userId, long postId) {
        return likedPostsRepository.existsLikedPostByUserIdAndPostId(userId, postId);
    }
}
