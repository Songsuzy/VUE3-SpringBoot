package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.HtmlUtil;
import com.example.entity.Account;
import com.example.entity.Category;
import com.example.entity.Introduction;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.mapper.CategoryMapper;
import com.example.mapper.IntroductionMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntroductionService {

    @Resource
    IntroductionMapper introductionMapper;

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    UserMapper userMapper;


    public List<Introduction> selectAll(Introduction introduction) {
        List<Introduction> introductions = introductionMapper.selectAll(introduction);
        for (Introduction dbIntroduction : introductions) {
            dbIntroduction.setDescription(HtmlUtil.cleanHtmlTag(dbIntroduction.getContent()));
        }
        return introductions;
    }

    public PageInfo<Introduction> selectPage(Integer pageNum, Integer pageSize, Introduction introduction) {
        Account account = TokenUtils.getCurrentUser();
        if (account.getRole().equals("USER")) {
            introduction.setUserId(account.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Introduction> list = introductionMapper.selectAll(introduction);
        for (Introduction dbIntroduction : list) {
            Integer categoryId = dbIntroduction.getCategoryId();
            Integer userId = dbIntroduction.getUserId();
            Category category= categoryMapper.selectById(categoryId);
            User user = userMapper.selectById(userId.toString());
            if (ObjUtil.isNotEmpty(category)) {
                dbIntroduction.setCategoryTitle(category.getTitle());
            }
            if (ObjUtil.isNotEmpty(user)) {
                dbIntroduction.setUserName(user.getName());
            }
        }
        return PageInfo.of(list);
    }

    public Introduction selectById(Integer id) {
        Introduction dbIntroduction = introductionMapper.selectById(id);
        Integer categoryId = dbIntroduction.getCategoryId();
        Integer userId = dbIntroduction.getUserId();
        Category category= categoryMapper.selectById(categoryId);
        User user = userMapper.selectById(userId.toString());
        if (ObjUtil.isNotEmpty(category)) {
            dbIntroduction.setCategoryTitle(category.getTitle());
        }
        if (ObjUtil.isNotEmpty(user)) {
            dbIntroduction.setUserName(user.getName());
            dbIntroduction.setUserAvatar(user.getAvatar());
        }
        return dbIntroduction;
    }

    public void add(Introduction introduction) {
        Account currentUser = TokenUtils.getCurrentUser();
        introduction.setUserId(currentUser.getId());
        introduction.setTime(DateUtil.now());
        introductionMapper.insert(introduction);
    }

    public void update(Introduction introduction) {
        introductionMapper.updateById(introduction);
    }

    public void deleteById(Integer id) {
        introductionMapper.deleteById(id);
    }

}