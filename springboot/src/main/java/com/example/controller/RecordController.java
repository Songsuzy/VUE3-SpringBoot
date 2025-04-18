package com.example.controller;

import com.example.common.Result;
import com.example.entity.Record;
import com.example.service.RecordService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController {
    @Resource
    RecordService recordService;

    @PostMapping("/add")
    public Result add(@RequestBody Record record) {
        recordService.add(record);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Record record) {
        recordService.update(record);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        recordService.deleteById(id);
        return Result.success();
    }


    @GetMapping("/selectAll")
    public Result selectAll(Record record) {
        List<Record> records = recordService.selectAll(record);
        return Result.success(records);
    }
/**
 * 分页查询
 * pageNum:当前的页码
 * pageSize:每页的个数
 */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             Record record
                             ) {
        PageInfo<Record> pageInfo = recordService.selectPage(pageNum, pageSize, record);
                return Result.success(pageInfo);

    }

}
