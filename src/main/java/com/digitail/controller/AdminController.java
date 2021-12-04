package com.digitail.controller;

import antlr.collections.List;
import com.digitail.model.User;
import com.digitail.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController{

    private UserRepo userRepo;

    @Autowired
    public AdminController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping()
    public String UsersList(Model model){
        model.addAttribute("users", userRepo.findAll());
        return "userList";
    }
}
