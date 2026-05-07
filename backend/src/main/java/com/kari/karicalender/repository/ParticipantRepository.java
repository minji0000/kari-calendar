package com.kari.karicalender.repository;

import com.kari.karicalender.domain.Participant;
import com.kari.karicalender.domain.Schedule;
import com.kari.karicalender.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    // 1. 특정 일정에 참여한 모든 사람의 명단을 가져옵니다.
    List<Participant> findBySchedule(Schedule schedule);

    // 2. 특정 일정에서 특정 유저가 이미 참여 중인지 확인합니다.
    Optional<Participant> findByScheduleAndUser(Schedule schedule, User user);

    // 3. 특정 일정의 공유키(shareKey)를 통해 참여자 목록을 가져오고 싶을 때 사용합니다.
    //Participant에 shareKey 컬럼이 없어도, 관계를 맺고 있는 Schedule을 타고 넘어가서 찾아옴
    List<Participant> findBySchedule_ShareKey(String shareKey);

    // 4. 내가(특정 유저가) 참여하고 있는 모든 일정 참여 정보를 가져옵니다.
    List<Participant> findByUser(User user);

    // 일정과 유저로 데이터 존재 여부 확인
    boolean existsByScheduleAndUser(Schedule schedule, User user);
}