package com.sri.ai.aidemo.rag;

import com.sri.ai.aidemo.services.GoogleAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class ProductDataBot {

	@Autowired
	private GoogleAiService service;

	@GetMapping("/showProductDataBot")
	public String showProductDataBot() {
		return "productDataBot";

	}

	@PostMapping("/productDataBot")
	public String productDataBot(@RequestParam String query, Model model) {
		String answer = service.answer(query);
		model.addAttribute("response",answer);
		return "productDataBot";

	}

}