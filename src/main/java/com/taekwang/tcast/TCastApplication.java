package com.taekwang.tcast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TCastApplication {

    public static void main(String[] args) {
        SpringApplication.run(TCastApplication.class, args);
    }

}
