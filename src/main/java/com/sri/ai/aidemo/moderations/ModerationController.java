package com.sri.ai.aidemo.moderations;

import com.sri.ai.aidemo.records.GeminiModerationResult;
import com.sri.ai.aidemo.services.GoogleAiService;
import org.springframework.ai.moderation.ModerationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class ModerationController {

	@Autowired
    private GoogleAiService service;

    @GetMapping("/showModeration")
    public String showChatPage() {
         return "moderation";
    }

    @PostMapping("/moderation")
    public String getChatResponse(@RequestParam("text") String text, Model model) {
        GeminiModerationResult moderate = service.moderate(text);
        model.addAttribute("response",moderate);
        return "moderation";
    }
}