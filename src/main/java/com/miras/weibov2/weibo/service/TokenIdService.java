package com.miras.weibov2.weibo.service;

import com.miras.weibov2.weibo.dto.TokenId;
import com.miras.weibov2.weibo.repository.TokenIdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenIdService {

    private final TokenIdRepository tokenIdRepository;

    public void save(TokenId tokenId){
        tokenIdRepository.save(tokenId);
    }

    public boolean isPresent(TokenId tokenId){
        return tokenIdRepository.isPresent(tokenId);
    }

}
