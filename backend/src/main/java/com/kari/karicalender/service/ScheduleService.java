package com.kari.karicalender.service;

import com.kari.karicalender.domain.Participant;
import com.kari.karicalender.domain.Schedule;
import com.kari.karicalender.domain.User;
import com.kari.karicalender.dto.schedule.ScheduleRequestDto;
import com.kari.karicalender.repository.ParticipantRepository;
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
    private final ParticipantRepository participantRepository; // 🌟 추가!

    /**
     * 새로운 일정 등록
     */
    @Transactional
    public String register(ScheduleRequestDto dto, User creator) {

        String shareKey = UUID.randomUUID().toString();

        // 1. 일정(Schedule) 정보 저장
        Schedule schedule = Schedule.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .creator(creator)
                .shareKey(shareKey)
                .build();

        scheduleRepository.save(schedule);

        // 2. 🌟 방장을 '참여자 명단'에 올리고 선택한 색상 부여
        Participant participant = Participant.builder()
                .schedule(schedule)
                .user(creator)
                .color(dto.getColor()) // DTO에 담긴 색상
                .build();

        participantRepository.save(participant);

        return shareKey;
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

    public Participant findParticipant(Schedule schedule, User user) {
        return participantRepository.findByScheduleAndUser(schedule, user)
                .orElse(null); // 혹은 예외 처리
    }

    @Transactional
    public void joinSchedule(String shareKey, User user, String selectedColor) {
        Schedule schedule = findByShareKey(shareKey);

        if (isParticipant(schedule, user)) return;

        Participant participant = Participant.builder()
                .schedule(schedule)
                .user(user)
                .color(selectedColor) // 🌟 사용자가 직접 고른 색상 저장!
                .build();

        participantRepository.save(participant);
    }

    // 특정 사용자가 해당 일정에 이미 참여 중인지 확인하는 함수
    public boolean isParticipant(Schedule schedule, User user) {
        // ParticipantRepository에서 일정과 유저 정보로 데이터가 있는지 조회합니다.
        return participantRepository.existsByScheduleAndUser(schedule, user);
    }
}
