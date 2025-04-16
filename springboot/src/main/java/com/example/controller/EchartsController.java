package com.example.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.Result;
import com.example.entity.Category;
import com.example.entity.Introduction;
import com.example.entity.User;
import com.example.service.CategoryService;
import com.example.service.IntroductionService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/echarts")
public class EchartsController {
    @Resource
    IntroductionService introductionService;
    @Resource
    CategoryService categoryService;
    @Resource
    UserService userService;

    @GetMapping("/pie")
    public Result pie() {
        List<Map<String,Object>> list = new ArrayList<>();
        List<Category> categories = categoryService.selectAll(new Category());
        List<Introduction> introductions = introductionService.selectAll(new Introduction());
        for (Category category : categories) {
            long count = introductions.stream().filter(x -> category.getId().equals(x.getCategoryId())).count();
            Map<String, Object> map = new HashMap<>();
            map.put("name",category.getTitle());
            map.put("value",count);
            list.add(map);
        }
        return Result.success(list);
    }

    @GetMapping("/bar")
    public Result bar() {
        Map<String, Long> map = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        List<String> xList = new ArrayList<>();
        List<Long> yList = new ArrayList<>();
        List<User> users = userService.selectAll(new User());
        List<Introduction> introductions = introductionService.selectAll(new Introduction());
        for (User user : users) {
            long count = introductions.stream().filter(x -> user.getId().equals(x.getUserId())).count();
            map.put(user.getName(),count);
        }
        //对map进行排序，按照value来倒序输出
        LinkedHashMap<String, Long> collect = map.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        for(String key : collect.keySet()) {
            xList.add(key);
            yList.add(collect.get(key));
        }

        if (xList.size() > 5&& yList.size() > 5) {
            xList = xList.subList(0,5);
            yList = yList.subList(0,5);
        }
        resultMap.put("xAxis",xList);
        resultMap.put("yAxis",yList);
        return Result.success(resultMap);
    }

    @GetMapping("/line")
    public Result line() {
        Map<String, Object> resultMap = new HashMap<>();
        List<Long> yList = new ArrayList<>();

        Date today = new Date();
        DateTime start = DateUtil.offsetDay(today, -7);
        List<String> xList = DateUtil.rangeToList(start, today, DateField.DAY_OF_YEAR).stream().map(DateUtil::formatDate).toList();
        List<Introduction> introductions = introductionService.selectAll(new Introduction());
        for (String day : xList) {
            long count = introductions.stream().filter(x -> ObjectUtil.isNotEmpty(x.getTime()) && x.getTime().contains(day)).count();
            yList.add(count);
        }
        resultMap.put("xAxis",xList);
        resultMap.put("yAxis",yList);
        return Result.success(resultMap);
    }
}
