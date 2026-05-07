package com.kari.karicalender.controller;

import com.kari.karicalender.config.auth.LoginUser;
import com.kari.karicalender.domain.Schedule;
import com.kari.karicalender.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/invite")
@RequiredArgsConstructor
public class InviteController {

    private final ScheduleService scheduleService;

    /**
     * 초대 링크 클릭 시 진입점
     * GET /invite/{shareKey}
     */
    @GetMapping("/{shareKey}")
    public String inviteEntry(@PathVariable String shareKey,
                              @AuthenticationPrincipal LoginUser loginUser,
                              Model model) {

        // [로직 2] 로그인이 되어있지 않을 경우
        // Spring Security 설정에 따라 @AuthenticationPrincipal이 null이면
        // 자동으로 로그인 페이지로 튕겨내거나, 아래처럼 직접 제어할 수 있어요.
        if (loginUser == null) {
            // 2-1. 로그인 페이지로 이동 (로그인 후 다시 여기로 돌아오게 세팅됨)
            return "redirect:/login";
        }

        // [로직 1-1] 참여자인지 확인
        Schedule schedule = scheduleService.findByShareKey(shareKey);
        boolean isParticipant = scheduleService.isParticipant(schedule, loginUser.getUser());

        // [로직 1-2] 참여자일 경우 상세페이지로 바로 이동
        if (isParticipant) {
            return "redirect:/schedule/detail/" + shareKey;
        }

        // [로직 1-3] 참여자가 아닐 경우 가입(참여) 페이지로 이동
        model.addAttribute("calendar", schedule);
        return "calendar/invite"; // 여기서 색상 고르는 invite.html을 보여줌
    }
}