package com.nwu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GraduationAlbumWxShareApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraduationAlbumWxShareApplication.class, args);
    }

}
