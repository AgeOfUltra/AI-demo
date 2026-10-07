package com.sri.ai.aidemo.embeddings;

import com.sri.ai.aidemo.services.GoogleAiService;
import com.sri.ai.aidemo.services.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class SimilarityFinder {

	@Autowired
	private GoogleAiService service;
	
	@GetMapping("/showSimilarityFinder")
	public String showSimilarityFinder() {
		return "similarityFinder";

	}

	@PostMapping("/similarityFinder")
	public String findSimilarity(@RequestParam String text1,@RequestParam String text2,Model model) {

		double similarity = service.findSimilarity(text1, text2);

		model.addAttribute("response",similarity);
		return "similarityFinder";

	}


}