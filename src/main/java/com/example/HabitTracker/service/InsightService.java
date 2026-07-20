package com.example.HabitTracker.service;

import com.example.HabitTracker.exception.ResourceNotFoundException;
import com.example.HabitTracker.model.Habit;
import com.example.HabitTracker.model.HabitLog;
import com.example.HabitTracker.repository.HabitLogRepository;
import com.example.HabitTracker.repository.HabitRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class InsightService {

    private final HabitRepository habitRepository;
    private final HabitLogRepository habitLogRepository;
    private final RestTemplate restTemplate;

    @Value("${groq.api.key}")
    private String groqApiKey;

    public InsightService(HabitRepository habitRepository,
                          HabitLogRepository habitLogRepository,
                          RestTemplate restTemplate){
        this.habitLogRepository=habitLogRepository;
        this.habitRepository=habitRepository;
        this.restTemplate=restTemplate;
    }

    public String getInsight(Long habitId){
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new ResourceNotFoundException("Habit not found"));
        LocalDate end=LocalDate.now();
        LocalDate start=end.minusDays(30);

        List<HabitLog> logs=habitLogRepository.findByHabitAndLogDateBetween(habit, start, end);

        String prompt=buildPrompt(habit, logs);
        return callGroq(prompt);
    }

    private String buildPrompt(Habit habit, List<HabitLog> logs){
        StringBuilder sb=new StringBuilder();
        sb.append("You are a habit tracking assistant. Analyze this habit data and give a brief, ")
                .append("encouraging insight in 2-3 sentences. Be specific about patterns you notice.\n\n");
        sb.append("Habit: ").append(habit.getName())
                .append(" (").append(habit.getTrackingType()).append(")\n");
        sb.append("Recent logs (last 30 days):\n");

        if(logs.isEmpty()){
            sb.append("No logs recorded yet.\n");
        }else{
            for (HabitLog log : logs) {
                sb.append("- ").append(log.getLogDate())
                        .append(": ").append(log.getValue()).append("\n");
            }
        }

        return sb.toString();

    }

    private String callGroq(String prompt){
        String url = "https://api.groq.com/openai/v1/chat/completions";

        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        Map<String, Object> message = Map.of(
                "role", "user",
                "content", prompt
        );

        Map<String, Object> body = Map.of(
                "model", "llama-3.3-70b-versatile",
                "messages", List.of(message)
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.POST, request, Map.class
        );
        Map<String, Object> responseBody = response.getBody();
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        Map<String, Object> firstChoice = choices.get(0);
        Map<String, Object> messageResponse = (Map<String, Object>) firstChoice.get("message");
        return (String) messageResponse.get("content");

    }


}
