package com.kari.karicalender.controller;

import com.kari.karicalender.config.auth.LoginUser;
import com.kari.karicalender.domain.User;
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

    /**
     * 🔑 로그인 페이지 이동 (에러 메시지 처리 포함)
     */
    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                            Model model) {

        // 주소창에 ?error 가 붙어서 들어왔을 때만 실행
        if (error != null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 맞지 않아요. 다시 확인해주세요! 🌸");
        }

        return "login"; // templates/login.html
    }


    /**
     * 회원가입 페이지 이동
     */
    @GetMapping("/join")
    public String joinPage() {
        return "join"; // templates/join.html 파일을 보여줍니다.
    }

    /**
     * 회원가입 로직
     */
    @PostMapping("/join")
    @ResponseBody
    public ResponseEntity<?> join(@ModelAttribute UserDto userDto, Model model) {
        try {
            userService.join(userDto);
            return ResponseEntity.ok().body("success");
        } catch (IllegalStateException e) {
            // 중복 오류 등을 400 에러와 함께 메시지로 전달
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public String profilePage(Model model, @AuthenticationPrincipal LoginUser loginUser) {

        // 1. 현재 로그인한 유저 정보가 잘 있는지 안전하게 체크
        if (loginUser != null) {
            // 2. 민지님이 커스텀한 LoginUser에서 실제 User 도메인 객체를 꺼내 Model에 담아줍니다.
            model.addAttribute("user", loginUser.getUser());
        }

        return "profile"; // templates/profile.html을 띄웁니다.
    }
}
