package com.miras.weibov2.weibo.dto;


import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class PostDto {

    Long id;
    String username;
    MultipartFile profileImage;
    String description;
    int numberOfLikes;
    int numberOfComments;
    MultipartFile[] postImages;
    Date createdDate;

}
