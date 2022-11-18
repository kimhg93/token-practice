package com.practice.api.controller;

import com.practice.api.common.security.jwt.JwtToken;
import com.practice.api.common.security.jwt.JwtTokenProvider;
import com.practice.api.common.security.jwt.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private String reToken;

    @CrossOrigin
    @PostMapping(value="/auth/token")
    public ResponseEntity<JwtToken> generate(@RequestBody TokenDto tokenDto){
        log.info("(line 50) {}, {}", tokenDto.getId(), tokenDto.getName());

        Map<String, Object> body = new HashMap<>();
        body.put("name", "name1234");

        log.info(tokenDto.toString());

        Authentication authentication = new UsernamePasswordAuthenticationToken(tokenDto.getId(), tokenDto.getName());
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication, body);

        log.info("Generate accessToken(line 61) : {}", jwtToken.getAccessToken());
        log.info("Generate refreshToken(line 62) : {}",jwtToken.getRefreshToken());
        reToken = jwtToken.getAccessToken();

        encryptTest("apiPracticeTest1234");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccessControlAllowOrigin(null);

        return ResponseEntity.ok()
                .headers(headers)
                .body(jwtToken);
    }

    @PostMapping(value="/api")
    public ResponseEntity<Map<String, Object>> postApiTest(@RequestBody String body){
        log.info("/api request body(line 76) : {} ", body);
        Map<String, Object> map = new HashMap<>();
        map.put("test1", "api called");
        map.put("test2", "ok");
        return ResponseEntity.status(HttpStatus.OK)
                .body(map);
    }

    @PostMapping("/auth/api")
    public Flux<Map> api(@RequestBody String refreshToken) {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8090/api")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        log.info("RefreshToken(line 91) : {}", refreshToken);

        Map<String, Object> body = new HashMap<>();
        body.put("body", "request test");

        log.info("/api/auth token(line 96) : {} ", reToken);
        Flux<Map> response = webClient.post()
                .headers( (h) -> h.setBearerAuth(reToken))
                .body(Flux.just(body), Map.class)
                .retrieve()
                .bodyToFlux(Map.class);

        log.info("/api/auth response body(line 103) : {} ",  response.blockFirst());
        return response;

    }


    public void encryptTest(String key){
        // ek=be sk=be, db=sk db=ek
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword("encryptKey123");

        log.info("general(line 136) : {}", key);

        String base64 =  Base64Utils.encodeToString(key.getBytes());
        String encrypt = encryptor.encrypt(base64);
        log.info("encode base64(line 140) : {}",  base64);
        log.info("encrypt base64(line 141) : {}", encrypt);


        String decrypt = encryptor.decrypt(encrypt);
        String decoded = new String(Base64Utils.decodeFromString(decrypt));
        log.info("decrypt base64(line 146) : {}", decrypt);
        log.info("decode base64(line 147) : {}", decoded);
    }
}
