package com.zgamelogic.app.authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthenticationWebController {
    @GetMapping("/login")
    public String postCodeGathering(@RequestParam String code, Model model) {
        model.addAttribute("code", code);
        return "login";
    }
}
