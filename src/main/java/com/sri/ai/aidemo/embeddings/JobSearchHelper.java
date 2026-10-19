package com.sri.ai.aidemo.embeddings;

import com.sri.ai.aidemo.services.GoogleAiService;
import com.sri.ai.aidemo.services.OpenAiService;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class JobSearchHelper {

	@Autowired
	private GoogleAiService service;

	@GetMapping("/showJobSearchHelper")
	public String showJobSearchHelper() {
		return "jobSearchHelper";

	}

	@PostMapping("/jobSearchHelper")
	public String jobSearchHelper(@RequestParam String query, Model model) {
		List<Document> documents = service.searchJob(query);
		model.addAttribute("response",documents);
		return "jobSearchHelper";

	}

}