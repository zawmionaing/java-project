package com.java.jobportal.config;

import com.java.jobportal.security.CustomeUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        
        @Autowired
        private CustomLoginSuccessHandler sucessHandler;
        @Autowired
        CustomeUserDetailsService userDetailsService;
        
        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public CsrfTokenRepository csrfTokenRepository() {
                HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
                repository.setHeaderName("X-CSRF-TOKEN");
                return repository;
        }
        
        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.authorizeHttpRequests(auth -> auth
                        .requestMatchers( "/login", "/register","/changePassword", "/forgot_password", "/requestForReset","/requestPasswordChange","/reset-password",
                                        "/companyRegisterForm", "/registerCompany", "/saveCompany","/accessDenied","/registerForm","/wrong","/passwordChange",
                                        "/saveUser", "/home","/","/jobs/**", "/about", "/contact/**", "/frontend/**", "/dashboard/**",
                                        "/company/img/**", "/job/img/**", "/images/**", "/js/**", "/webjars/**")
                        .permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/user/**").hasAuthority("USER")
                        .requestMatchers("/company/**", "/user/saveProfile/**").hasAuthority("COMPANY")
                    
                    ).formLogin(
                        form -> form
                            .loginPage("/login")
                            .successHandler(sucessHandler)
                            .failureUrl("/login?error=true")
                            .permitAll())
                    .logout(
                        logout -> logout
                            .logoutRequestMatcher(
                                new AntPathRequestMatcher("/logout"))
                            .permitAll()
                    
                    ).exceptionHandling(exh -> exh.accessDeniedPage("/accessDenied"))
                        .csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository()));
                
                return http.build();
        }
        
}