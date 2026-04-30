package com.kari.karicalender.service;

import com.kari.karicalender.domain.Schedule;
import com.kari.karicalender.domain.User;
import com.kari.karicalender.dto.schedule.ScheduleRequestDto;
import com.kari.karicalender.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // [기본값] 이 클래스의 모든 메서드는 일단 읽기 전용!
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    /**
     * 새로운 일정 등록
     */
    @Transactional
    public String register(ScheduleRequestDto dto, User creator) { // 반환 타입을 String으로!

        String shareKey = UUID.randomUUID().toString();

        Schedule schedule = Schedule.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .creator(creator)
                .shareKey(shareKey)
                .build();

        scheduleRepository.save(schedule);

        return shareKey; // 저장된 일정의 공유키를 반환!
    }

    /**
    * 공유키 찾기
    */
    public Schedule findByShareKey(String shareKey) {
        return scheduleRepository.findByShareKey(shareKey)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정이거나 잘못된 초대 링크입니다."));
    }

    /**
     * 내가 방장(creator_id)인 일정 목록 가져오기
     */
    @Transactional(readOnly = true)
    public List<Schedule> findMySchedules(Long userId) {
        // Repository에 findByCreatorId 메서드를 만들어야 해요!
        return scheduleRepository.findByCreatorId(userId);
    }
}
