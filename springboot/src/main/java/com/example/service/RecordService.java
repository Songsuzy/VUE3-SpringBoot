package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.Book;
import com.example.entity.Record;
import com.example.mapper.BookMapper;
import com.example.mapper.RecordMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {

    @Resource
    RecordMapper recordMapper;
    @Resource
    BookMapper bookMapper;
    

    public List<Record> selectAll(Record record) {
        return recordMapper.selectAll(record);
    }

    public PageInfo<Record> selectPage(Integer pageNum, Integer pageSize, Record record) {
        Account account = TokenUtils.getCurrentUser();
        if ("USER".equals(account.getRole())) {
            record.setUserId(account.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Record> list = recordMapper.selectAll(record);
        return PageInfo.of(list);
    }

    public void add(Record record) {
        record.setStatus("待审核");
        record.setTime(DateUtil.now());
        recordMapper.insert(record);
        Book book = bookMapper.selectById(record.getBookId());
        if (ObjectUtil.isNotEmpty(book)) {
            book.setNum(book.getNum() - 1);
            bookMapper.updateById(book);
        }
    }

    public void update(Record record) {
        recordMapper.updateById(record);
        Account currentUser = TokenUtils.getCurrentUser();
        if ("ADMIN".equals(currentUser.getRole()) && "审核拒绝".equals(record.getStatus())) {
            // 图书归还
            Book book = bookMapper.selectById(record.getBookId());
            if (ObjectUtil.isNotEmpty(book)) {
                book.setNum(book.getNum() + 1);
                bookMapper.updateById(book);
            }
        }
    }

    public void deleteById(Integer id) {
        recordMapper.deleteById(id);
    }

}