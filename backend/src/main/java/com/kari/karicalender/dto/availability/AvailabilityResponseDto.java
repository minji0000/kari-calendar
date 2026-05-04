package com.kari.karicalender.dto.availability;

import com.kari.karicalender.domain.Availability;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AvailabilityResponseDto {
    private LocalDate availableDate;
    private String color;

    public AvailabilityResponseDto(Availability entity) {
        this.availableDate = entity.getAvailableDate();
        this.color = entity.getColor();
    }
}