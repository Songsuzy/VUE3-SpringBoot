package com.example.controller;

import com.example.common.Result;
import com.example.entity.Introduction;
import com.example.service.IntroductionService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/introduction")
public class IntroductionController {
    @Resource
    IntroductionService introductionService;

    @PostMapping("/add")
    public Result add(@RequestBody Introduction introduction) {
        introductionService.add(introduction);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Introduction introduction) {
        introductionService.update(introduction);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        introductionService.deleteById(id);
        return Result.success();
    }


    @GetMapping("/selectAll")
    public Result selectAll(Introduction introduction) {
        List<Introduction> introductions = introductionService.selectAll(introduction);
        return Result.success(introductions);
    }
/**
 * 分页查询
 * pageNum:当前的页码
 * pageSize:每页的个数
 */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Introduction introduction
                             ) {
        PageInfo<Introduction> pageInfo = introductionService.selectPage(pageNum, pageSize, introduction);
                return Result.success(pageInfo);

    }

}
