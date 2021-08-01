package com.miras.weibov2.weibo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseEntity{

    @Column(name = "logo_address")
    private String logoAddress;

    @Column(name = "username", length = 20)
    @NotNull
    @NotEmpty
    private String username;

    @JsonIgnore
    @Column(name="password")
    @NotNull
    @NotEmpty
    private String password;

    @Column(name="email")
    @NotNull
    @NotEmpty
    private String email;

    @Column(name="is_verified")
    private boolean isVerified = false;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Post> myPosts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Comment> myComments;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_posts",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "post_id", referencedColumnName = "id")})
    private List<Post> likedPosts;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_comments",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "comment_id", referencedColumnName = "id")})
    private List<Comment> likedComments;




    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @JsonManagedReference
    private List<Role> roles;


    @Column(name = "verification_code")
    String verificationCode = null;





}
