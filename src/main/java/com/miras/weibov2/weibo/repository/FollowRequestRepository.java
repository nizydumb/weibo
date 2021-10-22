package com.miras.weibov2.weibo.repository;

import com.miras.weibov2.weibo.entity.FollowRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface FollowRequestRepository extends JpaRepository<FollowRequest, Long> {

    public boolean existsByFollowerIdAndFollowedId(long requestFromId, long requestToId);

    public FollowRequest findByFollowerIdAndFollowedId(long requestFromId, long requestToId);

}
