package com.digitail.controller;

import com.digitail.model.Role;
import com.digitail.model.User;
import com.digitail.repos.UserRepo;
import com.digitail.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
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
        if (user == null)
            model.addAttribute("flag", false);
        else model.addAttribute("flag", true);

        return "index";
    }

    @PostMapping("/registration")
    public String addUser(User user) {
        var userFromDb = userServiceImpl.findUserByUsername(user.getUsername());

        if (userFromDb != null) {
            return "redirect:/";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

        return "redirect:/";
    }
}
