package com.example.spartamatching_01.config;

import com.example.spartamatching_01.exception.CustomAccessDeniedHandler;
import com.example.spartamatching_01.exception.CustomAuthenticationEntryPoint;
import com.example.spartamatching_01.jwt.JwtAuthFiler;
import com.example.spartamatching_01.jwt.JwtUtil;

import com.example.spartamatching_01.repository.SignoutAccessTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {


    private final JwtUtil jwtUtil;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final SignoutAccessTokenRedisRepository signoutAccessTokenRedisRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();}

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers("/client/signup").permitAll()
                .antMatchers("/client/signin").permitAll()
                .antMatchers("/admin/signup").permitAll()
                .antMatchers("/admin/signin").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/client/**").permitAll()
                .antMatchers("/seller/**").permitAll()
                .antMatchers("/admin/**").permitAll()
                //.anyRequest().authenticated()
                .and().logout().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new JwtAuthFiler(jwtUtil, signoutAccessTokenRedisRepository), UsernamePasswordAuthenticationFilter.class);
        http.formLogin().disable();
        // 401 Error ??????, Authorization ???, ?????????????????? ????????? ??? ??????
        http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);
        // 403 Error ??????, ???????????? ????????? ???????????? ????????? ???????????? ?????? ??????
        http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);

        return http.build();
    }
}
