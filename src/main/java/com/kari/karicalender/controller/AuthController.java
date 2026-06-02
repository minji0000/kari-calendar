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

/**
 * 인증 및 계정 관리 컨트롤러
 * 로그인, 회원가입, 사용자 프로필 조회 등 사용자 인증 전반을 담당합니다.
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 로그인 페이지 이동
     * 시큐리티 로그인 실패 시 URL에 포함되는 error 파라미터를 감지하여 동적 에러 메시지를 전달합니다.
     *
     * @param error 시큐리티 로그인 실패 시 주소창에 들어오는 에러 여부 (?error)
     * @param logout 시큐리티 로그아웃 성공 시 주소창에 들어오는 로그아웃 여부 (?logout)
     * @param model 화면에 에러 메시지를 전달하기 위한 스프링 Model 객체
     * @return login.html 뷰 템플릿
     */
    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {

        // 스프링 시큐리티 로그인 실패 핸들러에 의해 ?error가 붙어서 들어왔을 때만 실행
        if (error != null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 맞지 않아요. 다시 확인해주세요! 🌸");
        }

        // 2. 로그아웃 성공 후 시큐리티가 ?logout을 붙여줬을 때만 실행
        if (logout != null) {
            model.addAttribute("logoutMessage", "안전하게 로그아웃되었습니다! 다음에 또 만나요 🌸");
        }

        return "login";
    }


    /**
     * 회원가입 페이지 이동
     * * @return join.html 뷰 템플릿
     */
    @GetMapping("/join")
    public String joinPage() {
        return "join";
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
            // UserService.join()에서 발생한 중복 가입 등의 예외 메시지를 프론트엔드로 전달
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 내 프로필(마이페이지) 화면 이동
     * 현재 인증된 세션 유저 정보를 꺼내와 마이페이지 화면에 바인딩합니다.
     *
     * @param model 뷰에 유저 정보를 넘겨주기 위한 스프링 Model 객체
     * @param loginUser 스프링 시큐리티 세션에 저장된 현재 로그인한 사용자 정보 (@AuthenticationPrincipal)
     * @return profile.html 뷰 템플릿 (로그인 유저가 없을 경우에도 안전하게 profile.html로 이동)
     */
    @GetMapping("/profile")
    public String profilePage(Model model, @AuthenticationPrincipal LoginUser loginUser) {

        // 세션에 로그인한 유저 정보가 존재하는지 안전성 체크 (NPE 방지)
        if (loginUser != null) {
            model.addAttribute("user", loginUser.getUser());
        }

        return "profile";
    }
}
