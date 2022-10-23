package kr.inhatc.spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import kr.inhatc.spring.files.service.StorageService;

@SpringBootApplication(scanBasePackages = {"kr.inhatc.spring"})
public class Spring2022Application {

    public static void main(String[] args) {
        SpringApplication.run(Spring2022Application.class, args);
    }
    
    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }

}
