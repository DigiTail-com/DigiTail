package com.digitail.controller;

import com.digitail.model.User;
import com.digitail.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class personalAreaController {

    private UserService userService;

    @Autowired
    public personalAreaController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String personalArea(Model model, @AuthenticationPrincipal User user){
        if (user == null)
            return "redirect:/";
        try{
            model.addAttribute("user", user);
            return "user_templates/personal_area";
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return "redirect:/";
    }

    @PostMapping("/editPerson")
    public String editPerson(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                             @AuthenticationPrincipal User userAuth){
//        if (bindingResult.hasErrors())
//            return "redirect:/user";

        userService.editUser(userAuth, user);
        return "redirect:/user";
    }
}
