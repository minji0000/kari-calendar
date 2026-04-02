package com.kari.karicalender.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "schedules")
public class Schedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator; // 방장

    @Column(nullable = false)
    private String title; // 예: "4월 클라이밍 모임"

    @Column(unique = true, nullable = false)
    private String shareKey; // 링크용 고유 키 (UUID 등)

    private String description; // 간단한 모임 설명
}