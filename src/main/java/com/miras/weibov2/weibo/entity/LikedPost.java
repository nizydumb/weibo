

package com.miras.weibov2.weibo.entity;


import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Any;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="liked_posts")
@Data
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LikedPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "post_id" )
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @CreatedDate
    @Column(name = "created")
    private Date created;




}
