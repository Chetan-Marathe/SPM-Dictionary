package com.spring.deepseek;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeepSeekService {

    private  OllamaChatModel chatModel;
    private List<Map<String, Object>> dataset;

    @Autowired
    public DeepSeekService(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
        loadDataset(); // Load dataset when the service starts
    }

    private void loadDataset() {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = new ClassPathResource("static/spm_data.json").getInputStream()) {
            dataset = objectMapper.readValue(inputStream, new TypeReference<>() {});
        } catch (IOException e) {
            System.err.println("Error loading dataset: " + e.getMessage());
            dataset = List.of();
        }
    }

    public String getFivePoints(String keyword) {
        for (Map<String, Object> entry : dataset) {
            if (entry.get("keyword").toString().equalsIgnoreCase(keyword)) {
                List<String> responseList = (List<String>) entry.get("response");
                return String.join("\n", responseList);
            }
        }

        String promptMessage = """
    Act as a strict, rule-following assistant. Your task is to return exactly five key points for the given keyword.

    IMPORTANT RULES:
    1. Do NOT include any "<think>" tags.
    2. Do NOT add explanations, introductions, or reasoning.
    3. Output ONLY the five numbered key points.
    4. Maintain the format below:

    Example Response (for 'Risk Management'):
    1. Risk identification helps in proactive project planning.
    2. Risk assessment quantifies impact and likelihood.
    3. Risk mitigation strategies reduce project uncertainties.
    4. Contingency planning ensures project resilience.
    5. Continuous monitoring improves risk response.

    Now, generate five key points for:
    """ + keyword;

        String response = chatModel.call(promptMessage).trim();

        // Clean up DeepSeek's stubborn behavior (if it still misbehaves)
        response = response.replaceAll("(?s)<think>.*?</think>", "").trim(); // Remove <think> section if present
        response = response.replaceAll("\\*\\*", ""); // Remove any unwanted Markdown formatting

        return response;
    }


}
