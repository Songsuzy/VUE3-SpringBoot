package com.example.controller;

import com.example.common.Result;
import com.example.entity.Admin;
import com.example.service.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    AdminService adminService;

    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Admin> admins = adminService.selectAll();
        return Result.success(admins);
    }
/**
 * 分页查询
 * pageNum:当前的页码
 * pageSize:每页的个数
 */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Admin admin
                             ) {
        PageInfo<Admin> pageInfo = adminService.selectPage(pageNum, pageSize, admin);
                return Result.success(pageInfo);

    }
}
