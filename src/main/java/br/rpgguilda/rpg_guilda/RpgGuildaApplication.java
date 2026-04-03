package br.rpgguilda.rpg_guilda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching       // Questão 2 - Habilita o Spring Cache
@EnableScheduling    // Questão 2 - Habilita agendamento para evict do cache
public class RpgGuildaApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpgGuildaApplication.class, args);
    }
}
