package com.bjpowernode.listener;

import com.bjpowernode.pojo.ProductType;
import com.bjpowernode.service.ProductTypeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

/**
 * @author liuke
 * @create 2021-09-08  22:20
 */
@WebListener
public class ProductTypeListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //从Spring容器中获取ProductTypeServiceImpl的对象
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext_*.xml");
        ProductTypeService productTypeService = (ProductTypeService) applicationContext.getBean("ProductTypeServiceImpl");
        List<ProductType> typeList = productTypeService.getAll();
        //放入全局应用域
        servletContextEvent.getServletContext().setAttribute("typeList",typeList);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
