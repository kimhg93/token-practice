package com.practice.api.config;

import com.practice.api.common.security.jwt.JwtAccessDeniedHandler;
import com.practice.api.common.security.jwt.JwtAuthenticationEntryPoint;
import com.practice.api.common.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .cors()
//                .and()
//                .csrf()
//                .disable()
//                .exceptionHandling()
//                .authenticationEntryPoint(unauthorizedHandler)
//                .and()
//                .sessionManagement() //(4)
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests() // (5)
//                .antMatchers("/auth/**")
//                .permitAll()
//                .antMatchers( "/**")
//                .authenticated()
//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint(unauthorizedHandler)
//                .accessDeniedHandler(accessDeniedHandler)
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .formLogin().disable().headers().frameOptions().disable();
//
//        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        // CSRF 설정 Disable
        http.cors()
                .and()
                .csrf()
                .disable()

                // exception handling 할 때 우리가 만든 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .accessDeniedHandler(accessDeniedHandler)

                // h2-console 을 위한 설정을 추가
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .anyRequest()
                .authenticated()   // 나머지 API 는 전부 인증 필요

                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
                .and()
                .apply(new JwtSecurityConfig(jwtTokenProvider));

        return http.build();

    }
}
