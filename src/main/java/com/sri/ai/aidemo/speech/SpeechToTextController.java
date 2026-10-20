package com.sri.ai.aidemo.speech;

import com.sri.ai.aidemo.services.GoogleAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sri.ai.aidemo.services.OpenAiService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Controller
public class SpeechToTextController {

    // Define the folder where images will be saved
    private static final String UPLOAD_DIR = "/home/snenavath/AI-demo/src/main/resources/images/audios/";
    
    @Autowired
    private GoogleAiService service;

    // Display the image upload form
    @GetMapping("/showSpeechToText")
    public String showUploadForm() {
        return "speechToText";
    }

    @PostMapping("/speechToText")
    public String uploadImage(String prompt, @RequestParam("file") MultipartFile file, Model model, 
    		RedirectAttributes redirectAttributes) {


        if(file.isEmpty()){
            model.addAttribute("message","Please select a file to upload");
            return "speechToText";
        }
        try{
            Path uploadDir = Paths.get(UPLOAD_DIR);
            if(Files.notExists(uploadDir)){
                Files.createDirectories(uploadDir);
            }

            Path path = uploadDir.resolve(file.getOriginalFilename());
            Files.write(path,file.getBytes(), StandardOpenOption.CREATE);

            String s = service.speechToText(path.toString());
            model.addAttribute("transcription",s);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "speechToText";
    }
}