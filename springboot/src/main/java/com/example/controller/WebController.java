package com.example.controller;

import com.example.common.Result;
import com.example.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class WebController {
    @Resource
    AdminService adminService;

    @GetMapping("/hello")
    public Result hello() {
//        int a=1/0;
        return Result.success("hello");
    }

    @GetMapping("/admin")
    public Result admin(String name) {
        String admin = adminService.admin(name);
        return Result.success(admin);
    }
}
