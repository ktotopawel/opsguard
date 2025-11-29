package com.ktotopawel.opsguard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OpsguardApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpsguardApplication.class, args);
    }

}
