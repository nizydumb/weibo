package com.miras.weibov2.weibo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="follow_requests")
@Data
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class FollowRequest extends BaseEntity{


    //Status - Active means Request is not approved by opposite side
    //Status - NotActive means Request is accepted
    //Status - Deleted means Request is not accepted and deleted by opposite side

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="follower_id", nullable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="followed_id", nullable = false)
    private User followed;




}
