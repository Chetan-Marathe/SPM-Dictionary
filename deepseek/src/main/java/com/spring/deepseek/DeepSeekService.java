package com.spring.deepseek;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class DeepSeekService {

    private final OllamaChatModel chatModel;
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
            // Log the error and initialize with an empty list
            System.err.println("Error loading dataset: " + e.getMessage());
            dataset = List.of();
        }
    }

    public String getFivePoints(String keyword) {
        // First, check if the keyword exists in the dataset
        for (Map<String, Object> entry : dataset) {
            if (entry.get("keyword").toString().equalsIgnoreCase(keyword)) {
                List<String> responseList = (List<String>) entry.get("response");
                return String.join("\n", responseList);
            }
        }

        // If not found, ask DeepSeek
        String promptMessage = "Explain '" + keyword + "' in exactly 5 key points. Keep each point clear and concise.";
        return chatModel.call(promptMessage);
    }
}