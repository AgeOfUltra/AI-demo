package com.sri.ai.aidemo.speech;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sri.ai.aidemo.services.OpenAiService;

@Controller
public class TextToSpeechController {

	@Autowired
	private OpenAiService service;

	// Display the image upload form
	@GetMapping("/showTextToSpeech")
	public String showUploadForm() throws IOException {
		return "textToSpeech";
	}

	@GetMapping("/textToSpeech")
	public ResponseEntity<byte[]> streamAudio(@RequestParam String text) {
		return null;
	}
}