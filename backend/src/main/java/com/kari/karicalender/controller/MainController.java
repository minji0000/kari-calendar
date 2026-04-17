package com.kari.karicalender.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    /*
    * 일정 목록(로그인 후 이동할 페이지)
    * */
    @GetMapping("/main")
    public String home() {
        return "main";
    }
}