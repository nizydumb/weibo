package com.miras.weibov2.weibo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity{

    @Column(name = "username", length = 20)
    @NotEmpty
    private String username;

    @Column(name = "full_name", length = 50)
    private String fullName;

    @Column(name = "bio", length = 100)
    private String bio;

    @Column(name = "website", length = 50)
    private String website;

    @JsonIgnore
    @Column(name="password")
    @NotEmpty
    private String password;

    @Column(name="email")
    @NotEmpty
    private String email;

    @Column(name="is_verified")
    private boolean isVerified = false;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Post> myPosts = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Comment> myComments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<LikedPost> likedPosts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<LikedComment> likedComments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "requestFrom")
    private List<FollowRequest> outgoingFollowRequests;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "requestTo")
    private List<FollowRequest> incomingFollowRequests;


//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "users_posts",
//            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "post_id", referencedColumnName = "id")})
//    private List<Post> likedPosts = new ArrayList<>();
//
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "users_comments",
//            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "comment_id", referencedColumnName = "id")})
//    private List<Comment> likedComments = new ArrayList<>();



    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @JsonManagedReference
    private List<Role> roles = new ArrayList<>();


    @Column(name = "verification_code")
    String verificationCode = null;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_followers",
                joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
                inverseJoinColumns = {@JoinColumn(name = "follower_id", referencedColumnName = "id")})
    private List<User> followers = new ArrayList<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_followings",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "following_id", referencedColumnName = "id")})
    private List<User> followings = new ArrayList<>();







}
