package com.kari.karicalender.service;

import com.kari.karicalender.domain.Availability;
import com.kari.karicalender.domain.Participant;
import com.kari.karicalender.domain.Schedule;
import com.kari.karicalender.domain.User;
import com.kari.karicalender.dto.availability.AvailabilityRequestDto;
import com.kari.karicalender.dto.availability.AvailabilityResponseDto;
import com.kari.karicalender.repository.AvailabilityRepository;
import com.kari.karicalender.repository.ParticipantRepository;
import com.kari.karicalender.repository.ScheduleRepository;
import com.kari.karicalender.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    @Transactional
    public void saveAll(List<AvailabilityRequestDto> dtoList, String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        if (dtoList.isEmpty()) return;

        Long scheduleId = dtoList.get(0).getScheduleId();
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다: " + scheduleId));

        for (AvailabilityRequestDto dto : dtoList) {
            Availability availability = Availability.builder()
                    .schedule(schedule)
                    .user(user)
                    .availableDate(dto.getAvailableDate())
                    .note(dto.getNote())
                    .build();

            availabilityRepository.save(availability);
        }
    }

    /**
     * 가능한 날짜 조회
     * @param shareKey
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<AvailabilityResponseDto> getMyAvailability(String shareKey, String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Schedule schedule = scheduleRepository.findByShareKey(shareKey)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다."));

        // 🌟 이 일정에서 이 유저의 색상을 찾아옵니다.
        Participant participant = participantRepository.findByScheduleAndUser(schedule, user)
                .orElseThrow(() -> new IllegalArgumentException("참여자 정보를 찾을 수 없습니다."));

        String userColor = participant.getColor();

        // 해당 유저의 날짜들을 가져와서 '그 유저의 색상'을 입혀서 반환합니다.
        return availabilityRepository.findByScheduleAndUser(schedule, user)
                .stream()
                .map(entity -> new AvailabilityResponseDto(entity, userColor))
                .collect(Collectors.toList());
    }
}