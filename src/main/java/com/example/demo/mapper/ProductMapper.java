package com.example.demo.mapper;

import com.example.demo.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    Product findById(@Param("id") Long id);

    List<Product> findByCategoryId(@Param("categoryId") Integer categoryId);

    void decreaseStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    void addProduct(Product product);
}
