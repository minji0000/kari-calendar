package com.kari.karicalender.controller;

import com.kari.karicalender.dto.availability.AvailabilityRequestDto;
import com.kari.karicalender.dto.availability.AvailabilityResponseDto;
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

        return ResponseEntity.ok("날짜가 성공적으로 저장되었습니다! ✨");
    }

    @GetMapping("/{shareKey}")
    public ResponseEntity<List<AvailabilityResponseDto>> getMyAvailability(
            @PathVariable("shareKey") String shareKey,
            Authentication authentication) {

        String userId = authentication.getName();

        // 서비스에 shareKey를 던져줍니다.
        List<AvailabilityResponseDto> myDates = availabilityService.getMyAvailability(shareKey, userId);

        return ResponseEntity.ok(myDates);
    }


}