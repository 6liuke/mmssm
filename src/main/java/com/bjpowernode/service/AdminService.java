package com.bjpowernode.service;

import com.bjpowernode.pojo.Admin;
import org.springframework.stereotype.Component;

/**
 * @author liuke
 * @create 2021-09-07  18:00
 */

public interface AdminService {
    Admin login(String name,String pwd);
}
