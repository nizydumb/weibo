package com.miras.weibov2.weibo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    long postId;
    long commentIdRepliedTo;
    String content;
}
