package com.example.HabitTracker.repository;

import com.example.HabitTracker.model.Habit;
import com.example.HabitTracker.model.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository

public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {

    List<HabitLog> findByHabitAndLogDateBetween(Habit habit, LocalDate startDate, LocalDate endDate);
}
