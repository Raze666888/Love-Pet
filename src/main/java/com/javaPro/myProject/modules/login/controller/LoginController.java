package com.javaPro.myProject.modules.login.controller;


import com.javaPro.myProject.common.model.R;

import com.javaPro.myProject.modules.sysuser.entity.Sysuser;
import com.javaPro.myProject.modules.sysuser.service.SysuserService;
import com.javaPro.myProject.modules.company.entity.Company;
import com.javaPro.myProject.modules.company.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    SysuserService userService;
    @Autowired
    CompanyService companyService;


    @GetMapping({"/login"})// 跳转到登陆界面
    public String login() {
        return "login";
    }

    @GetMapping({"/toLoginPage"})// 独立登录页面（无侧边栏）
    public String toLoginPage() {
        return "login";
    }

    @GetMapping({"/toRegisterPage"})// 独立注册页面（无侧边栏）
    public String toRegisterPage() {
        return "register";
    }

    /**
     * 登录接口
     * @param session
     * @param account 前端传过来的账号
     * @param password 前端传过来的密码
     * @return
     */
    @PostMapping({"/toLogin"})//映射一个POST请求,能够唯一标识一个方法体
    @ResponseBody//必要的，将java对象转为json格式的数据，从而展示在前端上
    public R toLogin(HttpSession session, String account, String password) {
        log.info("你当前登录的用户是：{}，密码是：{}", account, password);



        Sysuser sysuser= userService.queryByAccount(account);//admin的信息

        if (sysuser == null) {
            log.info("登录用户不存在");
            return R.fail("登录用户不存在");
        } else if (sysuser.getAccount() != null && !sysuser.getPassword().equalsIgnoreCase(password)) {
            log.info("密码错误");
            return R.fail("密码错误");
        } else if (sysuser.getAccount() != null && sysuser.getPassword().equalsIgnoreCase(password)) {

                session.setAttribute("session_user", sysuser);
                log.info("密码正确");
                Map<String,Object> map = new HashMap<>();
                map.put("userId",sysuser.getId());
                map.put("role",sysuser.getRole());
                return R.success(map);


        } else {
            return R.fail("密码和确认密码不一致");
        }
    }
    @PostMapping({"/toRegister"})//映射一个POST请求,能够唯一标识一个方法体
    @ResponseBody//必要的，将java对象转为json格式的数据，从而展示在前端上
    public R toRegister( Sysuser param) {


        Sysuser user = new Sysuser();//实例化一个User对象
    user.setAccount(param.getAccount());
    user.setPassword(param.getPassword());
    user.setRole(param.getRole());
    user.setUsername(param.getUsername());
    user.setPhonenumber(param.getPhonenumber());
    user.setSex(param.getSex());

        Sysuser user1= userService.queryByAccount(param.getAccount());

    if (user1 != null) {
        return R.fail("用户已经存在");
    }  else {
        if ("".equals(param.getPassword()) ){
            return R.fail("密码为空！");
        }
        Sysuser insert = userService.insert(user);

        // 如果注册的是服务商，自动创建Company记录
        if ("3".equals(param.getRole())) {
            Company company = new Company();
            company.setId(insert.getId()); // 使用用户ID作为Company的ID
            company.setPhonenumber(param.getPhonenumber());
            company.setCreateid(insert.getId());
            company.setStatus("1"); // 默认状态为启用
            companyService.insertWithId(company);
        }

        return R.success("注册成功！");
    }


    }
    @GetMapping({"/admin/logout"})
    @ResponseBody
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
