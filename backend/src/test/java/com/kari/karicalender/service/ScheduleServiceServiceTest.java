package com.kari.karicalender.service;

import com.kari.karicalender.domain.Schedule;
import com.kari.karicalender.domain.User;
import com.kari.karicalender.dto.schedule.ScheduleRequestDto;
import com.kari.karicalender.repository.ScheduleRepository;
import com.kari.karicalender.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class ScheduleServiceTest { // 이름 살짝 정리!

    @Autowired
    ScheduleService scheduleService;
    @Autowired ScheduleRepository scheduleRepository;
    @Autowired UserRepository userRepository; // 유저를 찾아오기 위해 추가!

    @Test
    @DisplayName("로그인한 사용자가 일정을 생성하고 상세 페이지용 키를 받는다")
    void registerWithUserTest() {
        // 1. Given (실제 DB에 있는 test 유저를 가져옴)
        // 만약 DB에 test가 없다면 회원가입 로직을 먼저 실행하거나 직접 넣어줘야 해요!
        User user = userRepository.findByUserId("test")
                .orElseThrow(() -> new IllegalArgumentException("테스트 유저가 없어요! 먼저 가입시켜주세요."));

        ScheduleRequestDto dto = new ScheduleRequestDto();
        dto.setTitle("민지의 첫 테스트 일정");
        dto.setDescription("테스트 일정입니다");

        // 2. When (서비스 실행)
        String shareKey = scheduleService.register(dto, user);

        // 3. Then (검증)
        Schedule found = scheduleService.findByShareKey(shareKey);

        assertThat(found.getTitle()).isEqualTo("민지의 첫 테스트 일정");
        assertThat(found.getCreator().getUserId()).isEqualTo("test"); // 만든 사람이 'test'가 맞는지 확인!
        assertThat(found.getShareKey()).isNotNull(); // 공유키가 잘 생성됐는지 확인!

        System.out.println("테스트 성공! 생성된 공유키: " + shareKey);
    }
}