package com.dcmall.back;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello from 김지훈!";
    }

    @PostMapping("/api/data")
    public String receiveData(@RequestBody String data) {
        // 데이터를 처리하는 로직
        System.out.println(data);
        return "Data received: " + data;
    }
}