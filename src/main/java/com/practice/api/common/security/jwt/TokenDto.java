package com.practice.api.common.security.jwt;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
public class TokenDto {

    private String id;
    private String name;
    private String password;
    private String auth;

    public TokenDto() {

    }

    public TokenDto(String id, String name, String password, String auth){
        this.id = id;
        this.name = name;
        this.password = password;
        this.auth = auth;
    }

}
