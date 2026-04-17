package com.kari.karicalender.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalendarController {

    /**
     * 새 일정 만들기 화면으로 이동
     */
    @GetMapping("/calendar/new")
    public String newCalendarForm() {
        // templates/calendar/new.html 파일을 찾아서 보여줍니다.
        return "calendar/new";
    }
}