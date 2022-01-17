package com.digitail.controller;

import com.digitail.config.WebSecurityConfig;
import com.digitail.model.Role;
import com.digitail.model.User;
import com.digitail.repos.UserRepository;
import com.digitail.service.impl.BasketServiceImpl;
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

    private UserRepository userRepository;
    private UserServiceImpl userServiceImpl;
    private BasketServiceImpl basketService;
    private WebSecurityConfig config;


    @Autowired
    public MainController(UserRepository userRepository,
                          UserServiceImpl userServiceImpl,
                          BasketServiceImpl basketService,
                          WebSecurityConfig config) {
        this.userRepository = userRepository;
        this.userServiceImpl = userServiceImpl;
        this.basketService = basketService;
        this.config = config;
    }

    @GetMapping()
    public String index(@AuthenticationPrincipal User user, Model model) {
        if (user == null) {
            model.addAttribute("flag", false);
            model.addAttribute("user", new User());
        } else {
            model.addAttribute("flag", true);
            model.addAttribute("user", user);
            model.addAttribute("basketSum",basketService.findSum(user));
        }

        return "index2";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        var userFromDb = userServiceImpl.findUserByUsername(user.getUsername());

        if (userFromDb != null) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            return "redirect:/";
        }

        user.setActive(true);
        if (user.getUsername().equals("Admin"))
            user.setRoles(Collections.singleton(Role.ADMIN));
        else user.setRoles(Collections.singleton(Role.USER));
        user.setMoney(0.0f);
        user.setPassword(config.getPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);

        return "redirect:/";
    }
}
