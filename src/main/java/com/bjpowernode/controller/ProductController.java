package com.bjpowernode.controller;

import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlShowColumnOutpuVisitor;
import com.bjpowernode.pojo.ProductInfo;
import com.bjpowernode.pojo.ProductType;
import com.bjpowernode.pojo.vo.ProductVo;
import com.bjpowernode.service.ProductInfoService;
import com.bjpowernode.utils.FileNameUtil;
import com.github.pagehelper.PageInfo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author liuke
 * @create 2021-09-08  15:20
 */

@Controller
@RequestMapping("/prod")
public class ProductController {

    @Autowired
    ProductInfoService productInfoService;

    String saveFileName ="";

    @RequestMapping("/getAll")
    public String getALl(HttpServletRequest request){
        List<ProductInfo> list = productInfoService.getAll();
        request.setAttribute("list",list);
        return "product";
    }

    @RequestMapping("/split")
    public String split(HttpServletRequest request){

        PageInfo pageInfo = null;
        Object vo = request.getSession().getAttribute("prodVo");
        if (vo != null){
            pageInfo = productInfoService.splitPageVo((ProductVo) vo,5);
            request.getSession().removeAttribute("prodVo");
        }else {
            //得到第一页的数据
            pageInfo = productInfoService.splitPage(1, 5);
        }
        request.setAttribute("info",pageInfo);
        return "product";
    }


    //ajax分页处理
    @ResponseBody
    @RequestMapping("/ajaxSplit")
    public void ajaxSplit(ProductVo vo, HttpSession httpSession){
        //获取当前页的数据
        PageInfo pageInfo = productInfoService.splitPageVo(vo, 5);
        httpSession.setAttribute("info",pageInfo);
    }


    //多条件查询的实现
    @ResponseBody
    @RequestMapping("/condition")
    public void condition(ProductVo vo,HttpSession session){
        List<ProductInfo> list = productInfoService.selectConditionSplitPage(vo);
        session.setAttribute("list",list);
    }

    //异步ajax上传文件处理
    @ResponseBody
    @RequestMapping("/ajaxImg")
    public Object ajaxImg(MultipartFile pimage,HttpServletRequest request){
        //提取生成文件名UUId加文件名后缀(.jpg、.png)
        saveFileName = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(pimage.getOriginalFilename());

        //得到项目中图片存储的路径
        String path = request.getServletContext().getRealPath("/image_big");

        //转存
        try {
            pimage.transferTo(new File(path+File.separator+saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }


        //返回客户端json对象,封装图片的路径,为了图片在页面中实现回显
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imgurl",saveFileName);

        return jsonObject.toString();
    }

    @RequestMapping("/save")
    public String save(ProductInfo info,HttpServletRequest request){

        info.setpImage(saveFileName);
        info.setpDate(new Date());
        int num = -1;
        try {
            num = productInfoService.save(info);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (num>0){
            request.setAttribute("msg","添加成功");
        }else {
            request.setAttribute("msg","添加失败请重试");
        }
        //清空saveFileName变量中的内容，为了下次增加或修改的异步ajax的上传处理
        saveFileName = "";

        return "forward:/prod/split.action";
    }


    @RequestMapping("/one")
    public String one(int pid,ProductVo vo, Model model,HttpSession session){

        ProductInfo info = productInfoService.getById(pid);
        model.addAttribute("prod",info);
        //将多条件放入Session中，更新完成后，读取session条件和页码进行处理
        session.setAttribute("prodVo",vo);

        return "update";
    }

    @RequestMapping("/update")
    public String update(ProductInfo info,HttpServletRequest request){
        //如果异步ajax图片上传,则saveFileName里有图片的名称，
        // 如果没有则，saveFileName="",实体类info使用隐藏表单域提供上来的pImage原始图片的名称
        if (!"".equals(saveFileName)){
            info.setpImage(saveFileName);
        }

        int num = -1;
        try {
            num = productInfoService.update(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0){
            //更新成功
            request.setAttribute("msg","更新成功");
        }else {
            //更新失败
            request.setAttribute("msg","更新失败");
        }

        saveFileName = "";


        return "forward:/prod/split.action";
    }


    @RequestMapping("/delete")
    public String delete(int pid,ProductVo vo,HttpServletRequest request){
        int num = -1;
        try {
            num = productInfoService.delete(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0){
            //删除成功
            request.setAttribute("msg","删除成功");
            request.getSession().setAttribute("deleteVo",vo);
        }else {
            request.setAttribute("msg","删除失败");
        }
        return "forward:/prod/deleteAjaxSplit.action";
    }

    @ResponseBody
    @RequestMapping(value = "/deleteAjaxSplit",produces = "text/html;charset=UTF-8")
    public Object deleteAjaxSplit(HttpServletRequest request){

        PageInfo pageInfo = null;
        Object vo = request.getSession().getAttribute("deleteVo");
        if (vo != null){
            pageInfo = productInfoService.splitPageVo((ProductVo) vo,5);
            request.getSession().removeAttribute("deleteVo");
        }else {
            pageInfo = productInfoService.splitPage(1,5);
        }
        //取得第一页的数据
        request.getSession().setAttribute("info",pageInfo);
        return request.getAttribute("msg");
    }

    //批量删除
    @RequestMapping("/deleteBatch")
    public String deleteBatch(String pids,ProductVo vo,HttpServletRequest request){

        String[] ps = pids.split(",");

        try {
            int num = productInfoService.deleteBatch(ps);
            if (num > 0){
                //删除成功
                request.setAttribute("msg","批量删除成功");
            }else {
                request.setAttribute("msg","批量删除失败！！！");
            }
        } catch (Exception e) {
            request.setAttribute("msg","商品不可删除");
        }
        return "forward:/prod/deleteAjaxSplit.action";
    }



}
