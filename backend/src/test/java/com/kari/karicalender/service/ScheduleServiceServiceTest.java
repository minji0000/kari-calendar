package com.kari.karicalender.service;

import com.kari.karicalender.domain.Schedule;
import com.kari.karicalender.domain.User;
import com.kari.karicalender.dto.schedule.ScheduleRequestDto;
import com.kari.karicalender.repository.ScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // 테스트 후 데이터를 롤백해줘서 DB가 깨끗하게 유지돼요!
class ScheduleServiceServiceTest {

    @Autowired ScheduleService scheduleService;
    @Autowired ScheduleRepository scheduleRepository;

    @Test
    @DisplayName("새로운 일정을 생성하고 공유키로 조회할 수 있어야 한다")
    void registerAndFindTest() {
        // 1. Given (준비)
        ScheduleRequestDto dto = new ScheduleRequestDto();
        dto.setTitle("민지랑 클라이밍 🧗‍♀️");
        dto.setDescription("퇴근하고 판교에서 모여요!");

        // creator는 null로 테스트하거나, 필요시 가짜 유저를 생성하세요.
        User mockUser = null;

        // 2. When (실행)
        String shareKey = scheduleService.register(dto, mockUser);
        Schedule foundSchedule = scheduleService.findByShareKey(shareKey);

        // 3. Then (검증)
        assertThat(foundSchedule.getTitle()).isEqualTo("민지랑 클라이밍 🧗‍♀️");
        assertThat(foundSchedule.getShareKey()).isEqualTo(shareKey);
        System.out.println("생성된 공유키: " + shareKey);
    }
}