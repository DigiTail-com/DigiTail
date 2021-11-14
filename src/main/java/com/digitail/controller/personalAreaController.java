package com.digitail.controller;

import com.digitail.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class personalAreaController {

    @GetMapping("/{id}")
    public String personalArea(@PathVariable("id") long id, Model model, @AuthenticationPrincipal User user){
        model.addAttribute("user", user);
        return "personalArea";
    }
}
