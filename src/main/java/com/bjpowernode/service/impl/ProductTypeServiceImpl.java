package com.bjpowernode.service.impl;

import com.bjpowernode.mapper.ProductTypeMapper;
import com.bjpowernode.pojo.ProductType;
import com.bjpowernode.pojo.ProductTypeExample;
import com.bjpowernode.service.ProductInfoService;
import com.bjpowernode.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuke
 * @create 2021-09-08  22:16
 */

@Service(value = "ProductTypeServiceImpl")
public class ProductTypeServiceImpl implements ProductTypeService {

    @Autowired
    ProductTypeMapper productTypeMapper;
    @Override
    public List<ProductType> getAll() {
        List<ProductType> list = productTypeMapper.selectByExample(new ProductTypeExample());
        return list;
    }
}
