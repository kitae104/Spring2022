package kr.inhatc.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/member/login")                                 // 로그인 페이지
                .defaultSuccessUrl("/")                                     // 성공 시 이동할 페이지
                .usernameParameter("email")                                 // 로그인에 사용할 파라미터
                .failureUrl("/member/login/error")      // 실패 시 이동할 페이지
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) // 로그아웃 페이지
                .logoutSuccessUrl("/");                                        // 로그아웃 성공 시 이동할 페이지

          // 페이지 접근에 대한 인증 처리
        http.authorizeRequests()
                .mvcMatchers("/css/**", "/js/**", "/img/**", "/images/**").permitAll()
                .mvcMatchers("/", "/thymeleaf/**", "/log/**", "/item/**", "/member/**", "/order/**", "/orders/**").permitAll()
                .mvcMatchers("/files/**", "/board/**").permitAll() 
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()); 

        // 예외 처리 
        http.csrf()
        .ignoringAntMatchers("/board/fileDelete", "/board/delete" )
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()); //csrf 토큰자동생성
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}