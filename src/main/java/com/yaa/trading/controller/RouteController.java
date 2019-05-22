package com.yaa.trading.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RouteController {

    @GetMapping(value = "/")
    public String index(){
        return "index";
    }

}
