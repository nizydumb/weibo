package com.miras.weibov2.weibo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "images")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Image extends BaseEntity {

    @Lob
    @Column(name = "content")
    private byte[] content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id", nullable = false)
    private Post post;

    @Column(name = "image_number")
    private long imageNumber;

}
