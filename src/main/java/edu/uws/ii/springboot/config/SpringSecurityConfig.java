package edu.uws.ii.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig{

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("*").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .requestMatchers("/resources/**","/signup").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/Customers/GetCustomers").permitAll()
                .requestMatchers("/Customers/ShowForm").hasAnyRole("KIEROWNIK", "ADMIN")
                .requestMatchers("/Customers/Create").hasAnyRole("ADMIN")
                .requestMatchers("/Customers/Delete").hasAnyRole("ADMIN")
                .requestMatchers("/Customers/Edit").hasAnyRole("ADMIN")
                .requestMatchers("/", "/index").permitAll()
                .requestMatchers("/db/**").access(new WebExpressionAuthorizationManager("hasRole('ADMIN') and hasRole('DBA')"))
                .anyRequest().authenticated());
        http.formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/App", true)
                .permitAll()
        );
        http.logout(logout -> logout.permitAll());
        http.csrf(config -> config.disable());
        //http.csrf(csrf -> csrf.ignoringRequestMatchers(toH2Console()).disable());
        //http.headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin()));

        http.exceptionHandling(config ->
                config.accessDeniedHandler((request, response, ex) -> {
                    response.sendRedirect("/url_error403");
                })
        );

        return http.build();
    }


}


