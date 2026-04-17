package com.kari.karicalender.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    // 일정 등록 화면 이동
    @GetMapping("/new")
    public String registerSchedulePage() {
        return "register-schedule";
    }

}