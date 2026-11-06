package com.sri.ai.aidemo.text;

import com.sri.ai.aidemo.services.GoogleAiService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;

@Controller
public class AnswerAnyThingStreamingController {
	@Autowired
	private GoogleAiService chatService;

	@GetMapping("/showAskAnythingStream")
	public String showAskAnything() {
		return "stream";
	}

	@GetMapping("/stream")
	@ResponseBody
	public Flux<String> askAnything(@RequestParam("message") String message) {

		return chatService.streamAnswer(message);
	}
}