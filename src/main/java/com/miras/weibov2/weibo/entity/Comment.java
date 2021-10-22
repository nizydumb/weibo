package com.miras.weibov2.weibo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "some_table")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity{


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="post_id", nullable = false)
    private Post post;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment")
    private List<LikedComment> likes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment", nullable = true)
    private Comment parentComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to_comment", nullable = true)
    private Comment repliedToComment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentComment")
    private List<Comment> childrenComments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "repliedToComment")
    private List<Comment> replies = new ArrayList<>();

    @Formula("(select count(*) from liked_comments where liked_comments.comment_id=id)")
    private int numberOfLikes;

    @Column(name="body", nullable = true)
    private String content;



}
