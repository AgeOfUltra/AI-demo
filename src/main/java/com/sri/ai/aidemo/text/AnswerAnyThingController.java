package com.sri.ai.aidemo.text;

import com.sri.ai.aidemo.services.GoogleAiService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class AnswerAnyThingController {

	@Autowired
    private GoogleAiService chatService;

    @GetMapping("/showAskAnything")
    public String showAskAnything() {
         return "askAnything";
    }

    @PostMapping("/askAnything")
    public String askAnything(@RequestParam("question") String question, Model model) {
        ChatResponse answer = chatService.generateAnswer(question);
        System.out.println(answer);
        model.addAttribute("question",question);
        model.addAttribute("answer",answer.getResult().getOutput().getText());
        return "askAnything";
    }
}