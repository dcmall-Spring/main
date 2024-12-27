package com.dcmall.back.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
//final 필드나 @NonNull 필드에 대해 자동으로 생성자(constructor)를 만듦
@RequiredArgsConstructor
public class redisTestController {
    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/test")
    public String testRedis() {
        redisTemplate.opsForValue().set("key", "Hello Redis!");
        return (String) redisTemplate.opsForValue().get("key");
    }
}
