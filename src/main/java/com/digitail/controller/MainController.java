package com.digitail.controller;

import com.digitail.model.Role;
import com.digitail.model.User;
import com.digitail.repos.UserRepo;
import com.digitail.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Collections;

@Controller
@RequestMapping("/")
public class MainController {

    private UserRepo userRepo;
    private UserServiceImpl userServiceImpl;

    @Autowired
    public MainController(UserRepo userRepo, UserServiceImpl userServiceImpl) {
        this.userRepo = userRepo;
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping()
    public String index(@AuthenticationPrincipal User user, Model model){
        if (user == null) {
            model.addAttribute("flag", false);
            model.addAttribute("user", new User());
        }
        else {
            model.addAttribute("flag", true);
            model.addAttribute("user", user);
        }

        return "index2";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        var userFromDb = userServiceImpl.findUserByUsername(user.getUsername());

        if (userFromDb != null) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()){
            return "redirect:/";
        }

        user.setActive(true);
        if (user.getUsername().equals("Admin"))
            user.setRoles(Collections.singleton(Role.ADMIN));
        else user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

        return "redirect:/";
    }
}
