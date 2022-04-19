package com.nwu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@SpringBootTest
class GraduationAlbumWxShareApplicationTests {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Test
    void contextLoads() {
        Boolean token = stringRedisTemplate.hasKey("token");
        System.out.println(token);
    }

}
