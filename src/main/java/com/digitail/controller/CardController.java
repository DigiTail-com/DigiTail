package com.digitail.controller;

import com.digitail.model.Card;
import com.digitail.model.User;
import com.digitail.service.UserService;
import com.digitail.service.impl.CardServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/card")
public class CardController {

    private final CardServiceImpl cardService;
    private final UserService userService;

    public CardController(CardServiceImpl cardService, UserService userService) {
        this.cardService = cardService;
        this.userService = userService;
    }

    @GetMapping("")
    public String addCard(@AuthenticationPrincipal User user,
                          Model model){

        if (user.getCard() == null)
            model.addAttribute("card", new Card());
        else
            model.addAttribute("card", user.getCard());

        return "product/payment_page";
    }

    @PostMapping("/addCard")
    public String addCardWrite(@ModelAttribute("card") Card card,
                               @AuthenticationPrincipal User user){

        card.setUser(user);
        user.setCard(card);

        cardService.save(card);
        userService.saveUser(user);

        return "redirect:/card";
    }
}
