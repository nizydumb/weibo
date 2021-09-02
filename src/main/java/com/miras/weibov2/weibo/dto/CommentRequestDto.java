package com.miras.weibov2.weibo.dto;

import lombok.Data;

@Data
public class CommentRequestDto {
    long postId;
    long commentIdRepliedTo;
    String content;
}
