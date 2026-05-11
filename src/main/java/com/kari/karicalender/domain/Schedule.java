package com.kari.karicalender.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "schedules")
public class Schedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(nullable = false)
    private String title;

    @Column(unique = true, nullable = false)
    private String shareKey;

    private String description;

    // 🌟 이 일정에 속한 참여자 명단을 바로 꺼내볼 수 있게 추가 (선택사항)
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Participant> participants = new ArrayList<>();
}