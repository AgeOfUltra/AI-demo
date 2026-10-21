package com.sri.ai.aidemo.speech;

import java.io.IOException;

import com.sri.ai.aidemo.services.GoogleAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sri.ai.aidemo.services.OpenAiService;

@Controller
public class TextToSpeechController {

	@Autowired
	private GoogleAiService service;

	// Display the image upload form
	@GetMapping("/showTextToSpeech")
	public String showUploadForm() throws IOException {
		return "textToSpeech";
	}

	@GetMapping("/textToSpeech")
	public ResponseEntity<byte[]> streamAudio(@RequestParam String text) {

		HttpHeaders headers= new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_TYPE,"audio/mpeg");
		headers.set(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=output.mp3");

		byte[] bytes = service.textToSpeech(text);


		return new ResponseEntity<>(bytes,headers, HttpStatus.OK);
	}
}