package com.kari.karicalender.service;

import com.kari.karicalender.domain.Availability;
import com.kari.karicalender.domain.Schedule;
import com.kari.karicalender.domain.User;
import com.kari.karicalender.dto.availability.AvailabilityRequestDto;
import com.kari.karicalender.repository.AvailabilityRepository;
import com.kari.karicalender.repository.ScheduleRepository;
import com.kari.karicalender.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveAll(List<AvailabilityRequestDto> dtoList, String userId) {
        // 1. 로그인한 유저 정보 가져오기
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        // 2. 어떤 일정(Schedule)인지 확인 (리스트의 첫 번째 항목 기준)
        if (dtoList.isEmpty()) return;

        Long scheduleId = dtoList.get(0).getScheduleId();
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다: " + scheduleId));

        // 3. DTO를 순회하며 Availability 객체 생성 후 저장
        for (AvailabilityRequestDto dto : dtoList) {
            Availability availability = Availability.builder()
                    .schedule(schedule)
                    .user(user)
                    .availableDate(dto.getAvailableDate())
                    .color(dto.getColor())
                    .note(dto.getNote())
                    .build();

            availabilityRepository.save(availability);
        }
    }
}