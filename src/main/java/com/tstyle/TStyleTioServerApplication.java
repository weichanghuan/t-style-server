package com.tstyle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableCaching
@ImportResource(locations = "classpath:applicationContext.xml")
public class TStyleTioServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TStyleTioServerApplication.class, args);
    }
}
