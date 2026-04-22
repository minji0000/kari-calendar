package com.kari.karicalender.controller;

import com.kari.karicalender.config.auth.LoginUser;
import com.kari.karicalender.domain.Schedule;
import com.kari.karicalender.dto.schedule.ScheduleRequestDto;
import com.kari.karicalender.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * 1. 새 일정 만들기 화면으로 이동
     * GET /schedule/new
     */
    @GetMapping("/new")
    public String newScheduleForm() {
        // templates/calendar/new.html (파일 위치는 민지님 구조에 맞춰 확인!)
        return "calendar/new";
    }

    /**
     * 2. 일정 생성 실행 (저장)
     * POST /schedule/register
     */
    @PostMapping("/register")
    public String register(@ModelAttribute ScheduleRequestDto dto,
                           @AuthenticationPrincipal LoginUser loginUser) {

        // 서비스에서 생성한 고유 shareKey를 반환받음
        String shareKey = scheduleService.register(dto, loginUser.getUser());

        // 상세 페이지 URL에 shareKey를 태워서 리다이렉트
        return "redirect:/schedule/detail/" + shareKey;
    }

    /**
     * 3. 일정(달력) 상세 화면 조회
     * GET /schedule/detail/{shareKey}
     */
    @GetMapping("/detail/{shareKey}")
    public String scheduleDetail(@PathVariable String shareKey, Model model) {
        // 서비스에서 shareKey로 Schedule 정보를 가져옴
        Schedule schedule = scheduleService.findByShareKey(shareKey);

        // detail.html에서 사용할 수 있도록 "calendar"라는 이름으로 모델에 담기
        model.addAttribute("calendar", schedule);

        return "calendar/detail";
    }

    /**
     * [테스트용] 일정 상세 화면 더미 데이터 조회
     */
    @GetMapping("/detail-test")
    public String scheduleDetailTest(Model model) {
        // 테스트를 위한 임시 객체 (나중에 삭제해도 됨!)
        Schedule dummy = Schedule.builder()
                .title("테스트용 우리 가족 달력 👨‍👩‍👧")
                .description("가족 모임 일정을 관리하는 달력입니다.")
                .shareKey("KARI-1234-TEST")
                .build();

        model.addAttribute("calendar", dummy);
        return "calendar/detail";
    }
}