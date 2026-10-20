package com.sri.ai.aidemo.imageprocessing;

import com.sri.ai.aidemo.services.GoogleAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ImageGenerationController {

	@Autowired
	private GoogleAiService service;

	@GetMapping("/showImageGenerator")
	public String showImageGenerator() {
		return "imageGenerator";

	}

	@PostMapping("/imageGenerator")
	public String imageGenerator(@RequestParam String prompt, Model model) {
		String s = service.generateImage(prompt);
		model.addAttribute("response", s);
		return "imageGenerator";
	}


}