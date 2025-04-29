package com.warehouse.universitywarehouse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails viceChancellor = User.builder()
                .username("vicechancellor")
                .password(passwordEncoder().encode("vc123"))
                .roles("VICE_CHANCELLOR")
                .build();

        UserDetails academicHead = User.builder()
                .username("academichead")
                .password(passwordEncoder().encode("academic123"))
                .roles("ACADEMIC_HEAD")
                .build();

        UserDetails admissionsDirector = User.builder()
                .username("admissions")
                .password(passwordEncoder().encode("admissions123"))
                .roles("ADMISSIONS_DIRECTOR")
                .build();

        UserDetails financeDirector = User.builder()
                .username("finance")
                .password(passwordEncoder().encode("finance123"))
                .roles("FINANCE_DIRECTOR")
                .build();

        UserDetails retentionOfficer = User.builder()
                .username("retention")
                .password(passwordEncoder().encode("retention123"))
                .roles("RETENTION_OFFICER")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(
                admin,
                viceChancellor,
                academicHead,
                admissionsDirector,
                financeDirector,
                retentionOfficer,
                user
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/index.html", "/css/**", "/js/**", "/images/**", "/login").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/etl/**").hasRole("ADMIN")
                        .requestMatchers("/api/dw/**").hasAnyRole("ADMIN", "VICE_CHANCELLOR", "ACADEMIC_HEAD",
                                "ADMISSIONS_DIRECTOR", "FINANCE_DIRECTOR", "RETENTION_OFFICER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}
