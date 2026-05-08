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

import java.util.List;

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

        if (loginUser == null) {
            return "redirect:/login";
        }

        Schedule schedule = scheduleService.findByShareKey(shareKey);
        boolean isParticipant = scheduleService.isParticipant(schedule, loginUser.getUser());

        if (isParticipant) {
            return "redirect:/schedule/detail/" + shareKey;
        }

        // 🌟 [추가] 이미 선택된 색상들을 가져와서 모델에 담아줘야 합니다!
        List<String> occupiedColors = scheduleService.getOccupiedColors(schedule);

        // 로그 찍어서 확인 (이제 콘솔에 잘 찍힐 거예요!)
        System.out.println("초대 페이지 진입 - 일정 ID: " + schedule.getId());
        System.out.println("이미 선택된 색상들: " + occupiedColors);

        model.addAttribute("calendar", schedule);
        model.addAttribute("occupiedColors", occupiedColors); // 👈 핵심!

        return "calendar/invite";
    }
}