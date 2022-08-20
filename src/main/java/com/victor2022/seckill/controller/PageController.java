package com.victor2022.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by: HuangFuBin
 * Date: 2018/7/9
 * Time: 12:36
 * Such description:
 */

@Controller
@RequestMapping("/page")
public class PageController {


    @GetMapping("login")
    public String loginPage(){
        return "login";
    }
}
