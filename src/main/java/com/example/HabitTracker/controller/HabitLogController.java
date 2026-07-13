package com.example.HabitTracker.controller;


import com.example.HabitTracker.dto.AddLogRequest;
import com.example.HabitTracker.model.HabitLog;
import com.example.HabitTracker.service.HabitLogService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user/habit/logs")
public class HabitLogController {

    private final HabitLogService habitLogService;

    public HabitLogController(HabitLogService habitLogService){
        this.habitLogService=habitLogService;
    }

    @PostMapping
    public String addLog(@RequestBody AddLogRequest request){
        habitLogService.addLog(request.getHabitId(), request.getValue(), request.getDate());
        return "Log added for";
    }

    @GetMapping("/{habitId}")
    public List<HabitLog> getLogsforHabit(@PathVariable Long habitId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate){
        return habitLogService.getLogsForHeatMap(habitId, startDate, endDate);
    }
}
