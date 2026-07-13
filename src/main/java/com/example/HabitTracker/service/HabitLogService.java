package com.example.HabitTracker.service;

import com.example.HabitTracker.model.Habit;
import com.example.HabitTracker.model.HabitLog;
import com.example.HabitTracker.repository.HabitLogRepository;
import com.example.HabitTracker.repository.HabitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.HabitTracker.model.TrackingType.BOOLEAN;
import static com.example.HabitTracker.model.TrackingType.NUMERICAL;

@Service
public class HabitLogService {

    private final HabitLogRepository habitLogRepository;
    private final HabitRepository habitRepository;

    public HabitLogService(HabitLogRepository habitLogRepository, HabitRepository habitRepository){
        this.habitLogRepository=habitLogRepository;
        this.habitRepository=habitRepository;
    }

    public HabitLog addLog(Long habitId, String value, LocalDate date){
        Habit h= habitRepository.findById(habitId)
                .orElseThrow(() -> new RuntimeException("Habit not found"));
        switch (h.getTrackingType()){
            case BOOLEAN -> {
                if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                    throw new IllegalArgumentException("Value must be true or false for a BOOLEAN habit");
                }
            }
            case NUMERICAL -> {
                try {
                    int num = Integer.parseInt(value);
                    if (num < h.getLow() || num > h.getHigh()) {
                        throw new IllegalArgumentException("Value must be between " + h.getLow() + " and " + h.getHigh());
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Value must be a number for a NUMERICAL habit");
                }
            }

        }
        HabitLog log=new HabitLog();
        log.setLogDate(date);
        log.setHabit(h);
        log.setValue(value);
        return habitLogRepository.save(log);
    }
    public List<HabitLog> getLogsForHeatMap(Long habitId, LocalDate startDate, LocalDate endDate){
        Habit h=habitRepository.findById(habitId).orElseThrow(
                ()-> new RuntimeException("Habit not found")
        );
        return habitLogRepository.findByHabitAndLogDateBetween(h, startDate, endDate);
    }
}
