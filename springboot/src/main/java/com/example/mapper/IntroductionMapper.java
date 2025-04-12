package com.example.mapper;

import com.example.entity.Introduction;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface IntroductionMapper {
    List<Introduction> selectAll(Introduction introduction);

    void insert(Introduction introduction);
    

    void updateById(Introduction introduction);

    @Delete("delete from `introduction` where id = #{id}")
    void deleteById(Integer id);

}
