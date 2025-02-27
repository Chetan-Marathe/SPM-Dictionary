package com.spring.deepseek;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KeywordService {

    private List<String> allKeywords;

    public KeywordService() {
        try {
            loadKeywords();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize keywords", e);
        }
    }

    private void loadKeywords() throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("static/keywords_only.txt").getInputStream()))) {
            allKeywords = reader.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            // If file not found, use a sample list for demonstration
            allKeywords = Arrays.asList(
                    "Software Project", "Project Management", "Risk Management",
                    "Work Breakdown Structure", "Gantt Chart", "PERT Chart",
                    "Agile Project Management", "Scrum", "Critical Path"
            );
        }
    }

    public List<String> getAllKeywords() {
        return allKeywords;
    }
}