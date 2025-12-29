package com.lynn.emergence.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Test {
    @RequestMapping("hello")
    public String toIndex() {
        return "index";
    }
}
