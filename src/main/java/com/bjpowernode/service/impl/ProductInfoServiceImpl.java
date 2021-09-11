package com.bjpowernode.service.impl;

import com.bjpowernode.mapper.ProductInfoMapper;
import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.ProductInfoExample;
import com.bjpowernode.pojo.vo.ProductVo;
import com.bjpowernode.service.ProductInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuke
 * @create 2021-09-08  15:17
 */

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    ProductInfoMapper productInfoMapper;
    @Override
    public List<ProductInfo> getAll() {

        return productInfoMapper.selectByExample(new ProductInfoExample());
    }

    @Override
    public PageInfo splitPage(int pageNum,int pageSize) {

        //分页插件使用PageHelper工具完成分页设置
        PageHelper.startPage(pageNum, pageSize);

        //进行PageInfo数据的封装
        //条件查询必须创建ProductInfoExample对象
        ProductInfoExample productInfoExample = new ProductInfoExample();

        //设置排序，按主键降序排序
        productInfoExample.setOrderByClause("p_id desc");

        //设置完排序之后取集合，一定要设置pagehelper在取集合之前
        List<ProductInfo> list = productInfoMapper.selectByExample(productInfoExample);

        PageInfo<ProductInfo> pageInfo = new PageInfo<>(list);

        return pageInfo;

    }

    @Override
    public int save(ProductInfo info) {

        return productInfoMapper.insert(info);
    }

    @Override
    public ProductInfo getById(int pid) {
        return productInfoMapper.selectByPrimaryKey(pid);
    }

    @Override
    public int update(ProductInfo info) {
        return productInfoMapper.updateByPrimaryKey(info);
    }

    @Override
    public int delete(int pid) {
        return productInfoMapper.deleteByPrimaryKey(pid);
    }

    @Override
    public int deleteBatch(String[] pids) {
        return productInfoMapper.deleteBatch(pids);
    }

    @Override
    public List<ProductInfo> selectConditionSplitPage(ProductVo vo) {

        return productInfoMapper.selectConditionSplitPage(vo);
    }

    @Override
    public PageInfo<ProductInfo> splitPageVo(ProductVo vo, int pageSize) {
        PageHelper.startPage(vo.getPage(),pageSize);
        List<ProductInfo> list = productInfoMapper.selectConditionSplitPage(vo);

        return new PageInfo<>(list);
    }
}
