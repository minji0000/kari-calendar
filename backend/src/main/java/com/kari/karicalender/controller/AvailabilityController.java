package com.kari.karicalender.controller;

import com.kari.karicalender.dto.availability.AvailabilityRequestDto;
import com.kari.karicalender.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
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

        String userId = authentication.getName();

        // 서비스 호출해서 DB 저장 (빌더로 만든 로직 작동!)
        availabilityService.saveAll(dtoList, userId);

        return ResponseEntity.ok("성공적으로 저장되었습니다! 이제 DB에서 확인해보세요. ✨");
    }
}