package com.example.HabitTracker.service;


import com.example.HabitTracker.exception.ResourceNotFoundException;
import com.example.HabitTracker.model.Habit;
import com.example.HabitTracker.model.User;
import com.example.HabitTracker.repository.HabitRepository;
import com.example.HabitTracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HabitService {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;

    public HabitService(HabitRepository habitRepository, UserRepository userRepository){
        this.habitRepository=habitRepository;
    this.userRepository=userRepository;}

    public Habit createHabit(Habit habit){
        User user=userRepository.findByUsername(habit.getUser().getUsername())
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));
        habit.setUser(user);
        habit.setCreatedDate(LocalDate.now());
        return habitRepository.save(habit);
    }

    public List<Habit> getHabitsByUser(String username){
        return habitRepository.findByUserUsername(username);
    }

    public String deleteHabit(Long id){
        habitRepository.deleteById(id);
        return "Habit deleted successfully";
    }
}
