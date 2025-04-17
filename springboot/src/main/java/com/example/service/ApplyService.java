package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Apply;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.mapper.ApplyMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplyService {

    @Resource
    ApplyMapper applyMapper;
    @Resource
    UserMapper userMapper;

    public List<Apply> selectAll(Apply apply) {
        return applyMapper.selectAll(apply);
    }

    public PageInfo<Apply> selectPage(Integer pageNum, Integer pageSize, Apply apply) {
        Account currentUser = TokenUtils.getCurrentUser();
        if ("USER".equals(currentUser.getRole())) {
            apply.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Apply> list = applyMapper.selectAll(apply);
//        for (Apply apply2 : list) {
//            Integer userId = apply2.getUserId();
//            User user = userMapper.selectById(userId.toString());
//            apply2.setUserName(user.getName());
//        }
        return PageInfo.of(list);
    }

    public void add(Apply apply) {
        Account account = TokenUtils.getCurrentUser();
        apply.setUserId(account.getId());
        apply.setTime(DateUtil.now());
        apply.setStatus("待审核");
        applyMapper.insert(apply);
    }

    public void update(Apply apply) {
        applyMapper.updateById(apply);
    }

    public void deleteById(Integer id) {
        applyMapper.deleteById(id);
    }

}