package com.miras.weibov2.weibo.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="posts")
@Data
public class Post extends BaseEntity{


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<Comment> comments;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "likedPosts")
    private List<User> usersLiked;

    @Column(name="description")
    private String description;

//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "post")
//    private List<Image> images;
    @Column(name="number_of_images")
    private int numberOfImages = 0;

}
