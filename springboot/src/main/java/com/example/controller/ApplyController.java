package com.example.controller;

import com.example.common.Result;
import com.example.entity.Apply;
import com.example.service.ApplyService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apply")
public class ApplyController {
    @Resource
    ApplyService applyService;

    @PostMapping("/add")
    public Result add(@RequestBody Apply apply) {
        applyService.add(apply);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Apply apply) {
        applyService.update(apply);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        applyService.deleteById(id);
        return Result.success();
    }


    @GetMapping("/selectAll")
    public Result selectAll(Apply apply) {
        List<Apply> applys = applyService.selectAll(apply);
        return Result.success(applys);
    }
/**
 * 分页查询
 * pageNum:当前的页码
 * pageSize:每页的个数
 */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Apply apply
                             ) {
        PageInfo<Apply> pageInfo = applyService.selectPage(pageNum, pageSize, apply);
                return Result.success(pageInfo);

    }

}
