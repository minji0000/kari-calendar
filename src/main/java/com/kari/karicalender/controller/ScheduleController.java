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
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * 1. 새 일정 만들기 화면으로 이동
     */
    @GetMapping("/new")
    public String newScheduleForm(@AuthenticationPrincipal LoginUser loginUser) {
        if (loginUser == null) {
            return "redirect:/login";
        }
        return "calendar/new";
    }

    /**
     * 2. 일정 생성 실행 (저장)
     */
    @PostMapping("/register")
    public String register(@ModelAttribute ScheduleRequestDto dto,
                           @AuthenticationPrincipal LoginUser loginUser) {
        if (loginUser == null) {
            return "redirect:/login";
        }

        // 서비스에서 생성한 고유 shareKey를 반환받음
        String shareKey = scheduleService.register(dto, loginUser.getUser());

        // URL 인코딩으로 방어
        String encodedShareKey = UriUtils.encode(shareKey, StandardCharsets.UTF_8);

        return "redirect:/schedule/detail/" + encodedShareKey;
    }

    /**
     * 3. 일정(달력) 상세 화면 조회
     */
    @GetMapping("/detail/{shareKey}")
    public String scheduleDetail(@PathVariable String shareKey,
                                 @AuthenticationPrincipal LoginUser loginUser,
                                 Model model) {
        if (loginUser == null) {
            return "redirect:/login";
        }

        Schedule schedule = scheduleService.findByShareKey(shareKey);
        if (schedule == null) {
            return "redirect:/schedule/main"; // 존재하지 않는 달력이면 메인으로
        }

        Participant participant = scheduleService.findParticipant(schedule, loginUser.getUser());

        // 만약 참여자가 아니라면 초대 페이지로 이동
        if (participant == null) {
            String encodedShareKey = UriUtils.encode(shareKey, StandardCharsets.UTF_8);
            return "redirect:/invite/" + encodedShareKey;
        }

        model.addAttribute("calendar", schedule);
        model.addAttribute("myColor", participant.getColor());

        return "calendar/detail";
    }

    /**
     * 4. 로그인한 유저의 일정 목록 조회
     */
    @GetMapping("/main")
    public String scheduleList(Model model, @AuthenticationPrincipal LoginUser loginUser) {
        if (loginUser == null) {
            return "redirect:/login";
        }

        Long userId = loginUser.getUser().getId();

        // 1. 방장으로서 생성한 일정
        List<Schedule> ownerCalendars = scheduleService.findMySchedules(userId);

        // 2. 참여자로서 공유받은 일정 (서비스에 로직이 있다면 연결, 없으면 null 방지용 처리)
        // List<Schedule> memberCalendars = scheduleService.findJoinedSchedules(userId);
        // model.addAttribute("memberCalendars", memberCalendars);
        model.addAttribute("ownerCalendars", ownerCalendars);

        return "calendar/main";
    }

    /**
     * 5. 일정 참여하기 실행
     */
    @PostMapping("/join/{shareKey}")
    public String joinSchedule(@PathVariable String shareKey,
                               @RequestParam String color,
                               @AuthenticationPrincipal LoginUser loginUser) {
        if (loginUser == null) {
            return "redirect:/login";
        }

        scheduleService.joinSchedule(shareKey, loginUser.getUser(), color);

        String encodedShareKey = UriUtils.encode(shareKey, StandardCharsets.UTF_8);
        return "redirect:/schedule/detail/" + encodedShareKey;
    }
}