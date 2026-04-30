package com.kari.karicalender.controller;

import com.kari.karicalender.dto.availability.AvailabilityRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody List<AvailabilityRequestDto> dtoList, Authentication authentication) {
        // 1. 데이터가 잘 넘어오는지 콘솔에서 확인!
        System.out.println("=== 날짜 저장 요청 발생 ===");
        System.out.println("보낸 날짜 개수: " + dtoList.size());

        if (!dtoList.isEmpty()) {
            System.out.println("첫 번째 날짜: " + dtoList.get(0).getAvailableDate());
            System.out.println("선택한 색상: " + dtoList.get(0).getColor());
        }

        System.out.println("작성자: " + authentication.getName());
        System.out.println("==========================");

        // 2. 일단 성공 응답만 보내기 (나중에 여기서 Service 호출해서 DB에 넣을 거예요!)
        return ResponseEntity.ok("서버까지 배달 완료!");
    }
}