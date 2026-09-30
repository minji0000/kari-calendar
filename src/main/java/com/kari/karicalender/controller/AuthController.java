package com.kari.karicalender.controller;

import com.kari.karicalender.config.auth.LoginUser;
import com.kari.karicalender.dto.user.UserDto;
import com.kari.karicalender.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {

        if (error != null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 맞지 않아요. 다시 확인해주세요! 🌸");
        }

        if (logout != null) {
            model.addAttribute("logoutMessage", "안전하게 로그아웃되었습니다! 다음에 또 만나요 🌸");
        }

        return "login";
    }

    @GetMapping("/join")
    public String joinPage() {
        return "join";
    }

    @PostMapping("/join")
    @ResponseBody
    public ResponseEntity<?> join(@ModelAttribute UserDto userDto) { // Form 제출 기준 (@ModelAttribute)
        try {
            userService.join(userDto);
            return ResponseEntity.ok().body("success");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public String profilePage(Model model, @AuthenticationPrincipal LoginUser loginUser) {
        // 비로그인 상태면 안전하게 로그인 페이지로 이동
        if (loginUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", loginUser.getUser());
        return "profile";
    }
}