package com.spring.deepseek;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Arrays;
import java.util.List;

@Controller
public class SPMController {

    private final DeepSeekService deepSeekService;
    private final KeywordService keywordService;

    public SPMController(DeepSeekService deepSeekService, KeywordService keywordService) {
        this.deepSeekService = deepSeekService;
        this.keywordService = keywordService;
    }

    @GetMapping("/")
    public String home(Model model) {
        // Get all keywords to display in the sidebar
        List<String> allKeywords = keywordService.getAllKeywords();
        model.addAttribute("keywords", allKeywords);
        return "home";
    }

    @GetMapping("/query")
    public String getAnswer(@RequestParam String keyword, Model model) {
        // Preserve your existing logic
        String responseText = deepSeekService.getFivePoints(keyword);
        List<String> responsePoints = Arrays.asList(responseText.split("\n"));

        // Get all keywords for the sidebar
        List<String> allKeywords = keywordService.getAllKeywords();

        model.addAttribute("keyword", keyword);
        model.addAttribute("response", responsePoints);
        model.addAttribute("keywords", allKeywords);

        return "home";
    }
}