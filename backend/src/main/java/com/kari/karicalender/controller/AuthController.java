package com.kari.karicalender.controller;

import com.kari.karicalender.domain.User;
import com.kari.karicalender.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 🔑 로그인 페이지 이동
     */
    @GetMapping("/login")
    public String loginForm() {
        return "login"; // templates/login.html을 찾아가요
    }

    /**
     * 🚀 실제 로그인 로직 처리
     */
    @PostMapping("/login")
    public String login(@RequestParam("userId") String userId,
                        @RequestParam("password") String password,
                        Model model) {
        try {
            User loginUser = userService.login(userId, password);
            // 로그인 성공!
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            // 로그인 실패 시 에러 메시지를 담아서 다시 로그인 페이지로!
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    /**
     * 회원가입
     */
    @PostMapping("/join")
    public String join(@RequestParam("userId") String userId,
                       @RequestParam("password") String password,
                       @RequestParam("nickname") String nickname,
                       @RequestParam(value = "realName", required = false) String realName,
                       @RequestParam(value = "email", required = false) String email,
                       @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                       @RequestParam(value = "birthday", required = false) String birthday,
                       @RequestParam(value = "gender", required = false) String gender,
                       @RequestParam("provider") String provider,
                       Model model) {
        try {
            User user = User.builder()
                    .userId(userId)
                    .password(password)
                    .nickname(nickname)
                    .realName(realName)
                    .email(email)
                    .phoneNumber(phoneNumber)
                    .birthday(birthday)
                    .gender(gender)
                    .provider(provider)
                    .build();

            userService.join(user);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "join";
        }
    }
}
