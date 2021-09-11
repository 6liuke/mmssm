package com.bjpowernode.service.impl;

import com.bjpowernode.mapper.AdminMapper;
import com.bjpowernode.pojo.Admin;
import com.bjpowernode.pojo.AdminExample;
import com.bjpowernode.service.AdminService;
import com.bjpowernode.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuke
 * @create 2021-09-07  18:00
 */

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Override
    public Admin login(String name, String pwd) {

        //添加查询条件
        AdminExample example = new AdminExample();

        example.createCriteria().andANameEqualTo(name);

        List<Admin> list = adminMapper.selectByExample(example);
        if (list.size()>0){
            Admin admin = list.get(0);

            //进行密码比对
            String pwdMd5 = MD5Util.getMD5(pwd);
            if (admin.getaPass().equals(pwdMd5)){
                return admin;
            }
        }

        return null;
    }
}
