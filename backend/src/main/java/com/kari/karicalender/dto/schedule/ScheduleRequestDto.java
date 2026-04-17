package com.kari.karicalender.dto.schedule;

import com.kari.karicalender.domain.Schedule;
import com.kari.karicalender.domain.User;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter @Setter
@NoArgsConstructor
//일정 등록 및 수정을 위한 요청 객체
public class ScheduleRequestDto {
    private String title;       // 일정 이름
    private String description; // 간단한 설명

    // DTO에서 엔티티로 변환하는 메서드
    public Schedule toEntity(User creator, String shareKey) {
        return Schedule.builder()
                .title(this.title)
                .description(this.description)
                .creator(creator)
                .shareKey(shareKey)
                .build();
    }
}