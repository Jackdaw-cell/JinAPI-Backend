package com.jackdawapi.jackdawapiapidoc;

import lombok.Data;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
@EnableConfigurationProperties({})
@Data
@ComponentScan
public class jackdawApiDocConfig {

    @Bean
    public Object jackdawApiDocClient() {
        return null;
    }

    @GetMapping("/getHello")
    public String get(){
        return "hello";
    }

}
