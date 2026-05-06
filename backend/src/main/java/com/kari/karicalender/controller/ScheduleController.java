package com.kari.karicalender.controller;

import com.kari.karicalender.config.auth.LoginUser;
import com.kari.karicalender.domain.Participant;
import com.kari.karicalender.domain.Schedule;
import com.kari.karicalender.dto.schedule.ScheduleRequestDto;
import com.kari.karicalender.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public String scheduleDetail(@PathVariable String shareKey,
                                 @AuthenticationPrincipal LoginUser loginUser, // 현재 유저 추가
                                 Model model) {
        Schedule schedule = scheduleService.findByShareKey(shareKey);

        // 🌟 현재 로그인한 유저가 이 일정에서 어떤 색인지 찾아 모델에 담기
        // 참여하지 않았다면 참여하기 페이지로 이동
        Participant participant = scheduleService.findParticipant(schedule, loginUser.getUser());

        model.addAttribute("calendar", schedule);
        model.addAttribute("myColor", participant != null ? participant.getColor() : null);

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

    /**
     * 로그인한 유저의 일정 가져옴
     */
    @GetMapping("/main")
    public String scheduleList(Model model, @AuthenticationPrincipal LoginUser loginUser) {
        // 1. 현재 로그인한 유저의 ID로 생성한 일정들을 가져옴
        List<Schedule> ownerCalendars = scheduleService.findMySchedules(loginUser.getUser().getId());

        // 2. HTML에 "ownerCalendars"라는 이름으로 전달
        model.addAttribute("ownerCalendars", ownerCalendars);

        // 3. 참여 중인 일정은 아직 로직이 없다면 빈 리스트로 전달 (에러 방지)
        model.addAttribute("memberCalendars", new ArrayList<>());

        return "calendar/main";
    }
}