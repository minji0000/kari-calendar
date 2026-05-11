package com.kari.karicalender.dto.availability;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
public class AvailabilityRequestDto {
    private Long scheduleId;        // 어떤 일정에 속하는지
    private LocalDate availableDate; // 선택한 날짜
    private String note;            // 해당 날짜의 간단한 메모
}