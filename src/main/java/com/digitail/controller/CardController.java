package com.digitail.controller;

import com.digitail.model.Card;
import com.digitail.model.User;
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

    public CardController(CardServiceImpl cardService) {
        this.cardService = cardService;
    }

    @GetMapping("")
    public String addCard(@AuthenticationPrincipal User user,
                          Model model){
        model.addAttribute("card", new Card());
        return null;
    }

    @PostMapping("/addCard")
    public String addCardWrite(@ModelAttribute("card") Card card,
                               @AuthenticationPrincipal User user){
        card.setUser(user);
        cardService.saveOrUpdate(card);
        return null;
    }
}
