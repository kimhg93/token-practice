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

        // CSRF ?????? Disable
        http.cors()
                .and()
                .csrf()
                .disable()

                // exception handling ??? ??? ????????? ?????? ???????????? ??????
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .accessDeniedHandler(accessDeniedHandler)

                // h2-console ??? ?????? ????????? ??????
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // ??????????????? ??????????????? ????????? ??????
                // ???????????? ????????? ???????????? ?????? ????????? ?????? ????????? Stateless ??? ??????
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // ?????????, ???????????? API ??? ????????? ?????? ???????????? ????????? ???????????? ????????? permitAll ??????
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .anyRequest()
                .authenticated()   // ????????? API ??? ?????? ?????? ??????

                // JwtFilter ??? addFilterBefore ??? ???????????? JwtSecurityConfig ???????????? ??????
                .and()
                .apply(new JwtSecurityConfig(jwtTokenProvider));

        return http.build();

    }
}
