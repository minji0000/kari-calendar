package com.kari.karicalender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KariCalenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(KariCalenderApplication.class, args);
    }

}
