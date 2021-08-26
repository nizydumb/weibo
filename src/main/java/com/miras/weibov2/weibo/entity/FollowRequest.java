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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="request_from_user_id")
    private User requestFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="request_to_user_id")
    private User requestTo;




}
