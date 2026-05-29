package com.kari.karicalender.controller;

import com.kari.karicalender.dto.availability.AvailabilityRequestDto;
import com.kari.karicalender.dto.availability.AvailabilityResponseDto;
import com.kari.karicalender.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 가능한 시간(Availability) 관리 API 컨트롤러
 */
@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    /**
     * 사용자가 선택한 가능한 날짜/시간 목록 저장
     * 프론트엔드에서 여러 개의 날짜 데이터를 리스트 형태로 받아 현재 로그인한 유저 정보와 함께 DB에 저장합니다.
     *
     * @param dtoList 사용자가 선택한 날짜 및 시간 정보 리스트 (RequestBody로 들어오는 JSON 배열)
     * @param authentication 현재 스프링 시큐리티 컨텍스트에 인증된 사용자 정보 (NPE 방지 및 ID 추출용)
     * @return 성공 메시지 ("날짜가 성공적으로 저장되었습니다! ✨")와 함께 200 OK 상태 코드 반환
     */
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