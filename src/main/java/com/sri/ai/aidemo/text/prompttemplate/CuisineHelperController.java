package com.sri.ai.aidemo.text.prompttemplate;

import com.sri.ai.aidemo.services.GoogleAiService;
import com.sri.ai.aidemo.text.prompttemplate.dto.CountryCuisines;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CuisineHelperController {
	@Autowired
    private GoogleAiService chatService;

    @GetMapping("/showCuisineHelper")
    public String showChatPage() {
         return "cuisineHelper";
    }

    @PostMapping("/cuisineHelper")
    public String getChatResponse(@RequestParam("country") String country, @RequestParam("numCuisines") String numCuisines,@RequestParam("language") String language,Model model) {
        CountryCuisines cusines = chatService.getCuisines(country,numCuisines,language);
        model.addAttribute("countryCuisines",cusines);
        return "cuisineHelper";
    }
}
