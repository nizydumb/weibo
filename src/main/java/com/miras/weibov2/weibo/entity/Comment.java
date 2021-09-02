package com.miras.weibov2.weibo.entity;

import lombok.Data;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "comments")
@Data
public class Comment extends BaseEntity{


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment")
    private List<LikedComment> likes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment")
    private Comment parentComment = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to_comment")
    private Comment repliedToComment = null;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentComment")
    private List<Comment> childrenComments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "repliedToComment")
    private List<Comment> replies;

    @Formula("(select count(*) from liked_comments where liked_comments.comment_id=id)")
    private int numberOfLikes;

    @Column(name="body")
    private String content;


}
