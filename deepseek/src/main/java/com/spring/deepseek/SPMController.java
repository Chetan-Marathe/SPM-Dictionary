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

    public SPMController(DeepSeekService deepSeekService) {
        this.deepSeekService = deepSeekService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
        // Returns home.html
    }

    @GetMapping("/query")
    public String getAnswer(@RequestParam String keyword, Model model) {
        String responseText = deepSeekService.getFivePoints(keyword);
        List<String> responsePoints = Arrays.asList(responseText.split("\n"));

        model.addAttribute("keyword", keyword);
        model.addAttribute("response", responsePoints);

        return "home";
    }
}
