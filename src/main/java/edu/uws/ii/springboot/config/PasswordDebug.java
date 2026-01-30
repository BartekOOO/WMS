package edu.uws.ii.springboot.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordDebug {

    @Bean
    CommandLineRunner printHashes(PasswordEncoder encoder) {
        return args -> {
            System.out.println("HASH(bart)  = " + encoder.encode("bart"));
            System.out.println("HASH(admin) = " + encoder.encode("admin"));
            System.out.println("HASH(Password123!) = " + encoder.encode("Password123!"));
        };
    }
}
