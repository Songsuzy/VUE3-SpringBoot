package com.example.controller;

import com.example.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {


    @GetMapping("/")
    public Result hello() {
        return Result.success("Hello Suzy");
    }

}
