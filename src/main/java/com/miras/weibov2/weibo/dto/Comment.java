package com.miras.weibov2.weibo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.miras.weibov2.weibo.entity.User;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;


public interface Comment {

    public long getId();

    Date getCreated();

    String getContent();

    @Value("#{target.likes.size()}")
    int getNumberOfLikes();

    @Value("#{target.author.getUsername()}")
    String getAuthor();

    @Value("#{target.author.getId()}")
    String getAuthorId();


    @Value("#{target.repliedToComment == null ? null : target.repliedToComment}")
    ParentComment getRepliedToComment();

    @Value("#{target.parentComment == null ? null : target.parentComment}")
    ParentComment getParentComment();

//    @Value("#{target.repliedToComment == null ? null : target.repliedToComment.getUsername()}")
//    String getUserId();

    @Value("#{target.childrenComments.size()}")
    int getNumberOfChildrenComments();

  //  List<Comment> getChildrenComments();


    interface ParentComment {
        long getId();

        @Value("#{target.author.getUsername()}")
        String getAuthor();

        @Value("#{target.author.getId()}")
        String getAuthorId();
    }
}