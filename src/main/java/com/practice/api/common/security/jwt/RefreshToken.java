package com.practice.api.common.security.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RefreshToken {


    private String key;
    private String value;

    @Builder
    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }
}