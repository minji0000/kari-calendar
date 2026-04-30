package com.kari.karicalender.repository;

import com.kari.karicalender.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findByShareKey(String shareKey);

    // creator_id 컬럼으로 일정을 찾는 쿼리 자동 생성
    List<Schedule> findByCreatorId(@Param("creatorId") Long creatorId);
}