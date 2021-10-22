package com.miras.weibov2.weibo.dto;

import lombok.*;


@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponse {

    Post post;
    PostMetaData postMetaData;


}
