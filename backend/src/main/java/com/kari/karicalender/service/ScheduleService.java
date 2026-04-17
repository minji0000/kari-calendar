package com.kari.karicalender.service;

import com.kari.karicalender.domain.Schedule;
import com.kari.karicalender.domain.User;
import com.kari.karicalender.dto.schedule.ScheduleRequestDto;
import com.kari.karicalender.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // [기본값] 이 클래스의 모든 메서드는 일단 읽기 전용!
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    /**
     * 새로운 일정 등록
     */
    @Transactional //(등록/수정삭제) 가능
    public Long register(ScheduleRequestDto dto, User creator) {

        //공유키 생성
        String shareKey = UUID.randomUUID().toString();

        Schedule schedule = Schedule.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .creator(creator)
                .shareKey(shareKey)
                .build();

        return scheduleRepository.save(schedule).getId();
    }

}
