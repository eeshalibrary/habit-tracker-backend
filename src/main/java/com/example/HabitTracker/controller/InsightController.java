package com.example.HabitTracker.controller;

import com.example.HabitTracker.service.InsightService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/habits")
public class InsightController {
    private final InsightService insightService;

    public InsightController(InsightService insightService){
        this.insightService=insightService;
    }

    @GetMapping("/{id}/insights")
    public String getInsight(@PathVariable Long id){
        return insightService.getInsight(id);
    }
}
