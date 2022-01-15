package com.digitail.controller;

import com.digitail.model.User;
import com.digitail.service.UserService;
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
    private final UserService userService;

    @Autowired
    public BasketController(BasketServiceImpl basketService, UserService userService) {
        this.basketService = basketService;
        this.userService = userService;
    }

    @GetMapping("")
    public String basketShow(@AuthenticationPrincipal User user,
                             Model model){
        var basketGoods = basketService.findAllProductsByUser(user);
        model.addAttribute("products", basketGoods);
        model.addAttribute("user", user);
        model.addAttribute("card", user.getCard());
        return "redirect:/";
    }

    @DeleteMapping(value = "/delete/{id}")
    public String basketDeleteProduct(@PathVariable Long id, @AuthenticationPrincipal User user){

        basketService.deleteById(id);

        user.setBasketGoods(basketService.findAllBasketGoodsByUser(user));
        user.setBasketCosts(basketService.findSum(user));
        userService.saveUser(user);
        return "redirect:/basket";
    }
}
