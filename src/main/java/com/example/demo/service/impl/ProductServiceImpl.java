package com.example.demo.service.impl;

import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl {

    @Autowired
    private ProductMapper productMapper;

    public Product getProduct(Long id) {
        return productMapper.findById(id);
    }

    /**
     * 减少指定产品的库存数量
     * 此方法用于在执行订单时减少产品库存，确保库存信息的准确性
     *
     * @param id 产品的唯一标识符，用于定位需要更新的产品
     * @param quantity 需要减少的库存数量，通常与订单中产品的数量相对应
     */
    public void reduceStock(Long id, Integer quantity) {
        productMapper.decreaseStock(id, quantity);
    }
}
