package com.miras.weibov2.weibo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "comments")
@Data
public class Comment extends BaseEntity{


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "likedComments")
    private List<User> usersLiked;

    @Column(name="body")
    private String body;


}
