package com.digitail.controller;

import com.digitail.model.User;
import com.digitail.service.impl.BasketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/basket")
public class BasketController {

    private final BasketServiceImpl basketService;

    @Autowired
    public BasketController(BasketServiceImpl basketService) {
        this.basketService = basketService;
    }

    @GetMapping("")
    public String basketShow(@AuthenticationPrincipal User user,
                             Model model){
        model.addAttribute("products", basketService.findAll());
        return null;
    }

    @DeleteMapping(value = "/delete/{id}")
    public String basketDeleteProduct(@PathVariable Long id){
        basketService.deleteById(id);
        return "redirect:/basket";
    }


}
