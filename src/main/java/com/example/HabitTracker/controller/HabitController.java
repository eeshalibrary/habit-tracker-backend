package com.example.HabitTracker.controller;


import com.example.HabitTracker.model.Habit;
import com.example.HabitTracker.service.HabitService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user/habit/")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService){
        this.habitService=habitService;
    }

    @PostMapping
    public Habit createHabit(@RequestBody Habit habit){
        return habitService.createHabit(habit);
    }

    @GetMapping("/{username}")
    public List<Habit> getHabitsforUser(@PathVariable String username){
        return habitService.getHabitsByUser(username);
    }

    @DeleteMapping("/{id}")
    public String deleteHabit(@PathVariable Long id){
        habitService.deleteHabit(id);
        return "Habit deleted";
    }


}
