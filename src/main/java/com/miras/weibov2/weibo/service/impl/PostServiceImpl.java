package com.miras.weibov2.weibo.service.impl;

import com.miras.weibov2.weibo.dto.PostLoadDto;
import com.miras.weibov2.weibo.dto.PostProjection;
import com.miras.weibov2.weibo.entity.User;
import com.miras.weibov2.weibo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PostServiceImpl {
    private final PostRepository postRepository;



    public Stream<PostProjection> loadPostDtoProfileByUserId(long userId, int pageNumber) {
        User user = new User();
        user.setId(userId);
        return postRepository.findAllByUserOrderByCreatedDesc(user, PageRequest.of(pageNumber,20)).stream();
    }

    public Stream<PostProjection> loadPostDtoFeedByUserId(List<Long> followingIds, int pageNumber) {
        return postRepository.findAllByUserIdInOrderByCreatedDesc(followingIds, PageRequest.of(pageNumber, 20)).stream();

    }
}
