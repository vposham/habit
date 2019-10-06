package edu.hackathon.habit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FeController {


    @GetMapping("/app")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="index3") String name, Model model) {
        model.addAttribute("name", name);
        return "index3.html";
    }
}
