package com.kari.karicalender.repository;

import com.kari.karicalender.domain.Availability;
import com.kari.karicalender.domain.Schedule;
import com.kari.karicalender.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    // 특정 일정에서 특정 유저가 선택한 날짜 리스트만 가져오기!
    List<Availability> findByScheduleAndUser(Schedule schedule, User user);
}