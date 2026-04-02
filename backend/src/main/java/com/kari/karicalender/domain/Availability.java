package com.kari.karicalender.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "availabilities")
public class Availability extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule; // 어느 약속(링크)에 속해 있는지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 참여한 사람

    @Column(nullable = false)
    private LocalDate availableDate; // "나 이때 돼!"라고 찍은 날짜

    @Column(nullable = false)
    private String color; // 해당 약속에서 점유한 고유 컬러 (20개 중 하나)

    private String note; // "오후만 가능" 같은 가벼운 메모
}