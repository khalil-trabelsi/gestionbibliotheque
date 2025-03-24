package com.isima.gestionbibliotheque;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCaching
public class GestionbibliothequeApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionbibliothequeApplication.class, args);
    }

}
