package com.miras.weibov2.weibo.service;

import com.miras.weibov2.weibo.entity.Image;
import com.miras.weibov2.weibo.entity.Post;
import com.miras.weibov2.weibo.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public void uploadImage(MultipartFile[] files, Long postId) throws Exception{
        Post post = new Post();
        post.setId(postId);
        Image image;
        int order = 0;
        for(MultipartFile file : files){
            image = new Image();
            image.setPost(post);
            image.setContent(file.getBytes());
            image.setImageNumber(order);
            imageRepository.save(image);
            order++;
        }
    }

    public Resource getImage(long postId, long imageId) {
         byte[] image;
         if(imageRepository.existsByPostIdAndImageNumber(postId, imageId)) {
            image = imageRepository.findByPostIdAndImageNumber(postId, imageId).getContent();
         }
         else throw new ResponseStatusException(HttpStatus.NO_CONTENT);
         return new ByteArrayResource(image);
        }
}

