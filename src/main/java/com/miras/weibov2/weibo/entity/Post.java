package com.miras.weibov2.weibo.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.miras.weibov2.weibo.repository.LikedPostsRepository;
import com.miras.weibov2.weibo.repository.PostRepository;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="posts")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Post extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<Image> images = new ArrayList<>();


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<LikedPost> likes = new ArrayList<>();

    @Column(name="description")
    private String description;






}
