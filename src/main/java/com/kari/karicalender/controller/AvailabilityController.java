package com.kari.karicalender.controller;

import com.kari.karicalender.dto.availability.AvailabilityRequestDto;
import com.kari.karicalender.dto.availability.AvailabilityResponseDto;
import com.kari.karicalender.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody List<AvailabilityRequestDto> dtoList, Authentication authentication) {
        // 비인증 사용자 접근 시 401 Unauthorized 방어
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        String userId = authentication.getName();
        availabilityService.saveAll(dtoList, userId);

        return ResponseEntity.ok("날짜가 성공적으로 저장되었습니다! ✨");
    }

    @GetMapping("/{shareKey}")
    public ResponseEntity<List<AvailabilityResponseDto>> getMyAvailability(
            @PathVariable("shareKey") String shareKey,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = authentication.getName();
        List<AvailabilityResponseDto> myDates = availabilityService.getMyAvailability(shareKey, userId);

        return ResponseEntity.ok(myDates);
    }
}