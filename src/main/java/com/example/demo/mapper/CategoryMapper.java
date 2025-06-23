package com.example.demo.mapper;

import com.example.demo.dto.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<Category> findAll();

    Category findById(@Param("id") Integer id);

    void addCategory(Category category);
}
