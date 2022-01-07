package com.digitail.controller;

import com.digitail.model.Card;
import com.digitail.model.Product;
import com.digitail.model.Status;
import com.digitail.model.User;
import com.digitail.repos.ProductRepository;
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
    private ProductRepository productRepository;
    private Model errorModel;

    @Autowired
    public personalAreaController(UserService userService, ProductRepository productRepository) {
        this.userService = userService;
        this.productRepository = productRepository;
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

    @GetMapping("/products")
    public String showUserProducts(@AuthenticationPrincipal User user, Model model){
        var products = productRepository.findAllByUser(user);
        model.addAttribute("products", products);
        return "product/user_products";
    }

    @GetMapping("/showProduct/{id}")
    public String showUserProduct(@PathVariable long id, @AuthenticationPrincipal User user, Model model){
        model.addAttribute("product", productRepository.findById(id));
        return "product/user_product_page";
    }

    @PostMapping("/editProduct/{id}")
    public String editUserProduct(@PathVariable long id,
                                  @ModelAttribute("product") @Valid Product product,
                                  @AuthenticationPrincipal User user){
        var productFromDB = productRepository.findById(id);
        productFromDB.setStatus(Status.AWAITING);
        productFromDB.setName(product.getName());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setCategory(product.getCategory());

        productRepository.save(productFromDB);

        return "redirect:/user/showProduct/" + id;
    }

    @GetMapping("/addMoney")
    public String addMoney(@AuthenticationPrincipal User user,
                           Model model){
        try{
            if (errorModel.containsAttribute("incorrectCard"))
                model.addAttribute("incorrectCard", true);
        }catch (Exception exception){
            model.addAttribute("incorrectCard", false);
        }

        try{
            if (errorModel.containsAttribute("notEnoughMoney"))
                model.addAttribute("notEnoughMoney", true);
        }catch (Exception exception){
            model.addAttribute("notEnoughMoney", false);
        }

        try{
            if (errorModel.containsAttribute("moneyShouldBePositive"))
                model.addAttribute("moneyShouldBePositive", true);
        }catch (Exception exception){
            model.addAttribute("moneyShouldBePositive", false);
        }

        errorModel = null;

        model.addAttribute("card", new Card());
        return null;
    }

    @PostMapping("/addMoney/{numberOfMoney}")
    public String addMoneyProcess(@PathVariable Float numberOfMoney,
                                  @ModelAttribute("card") Card card,
                                  @AuthenticationPrincipal User user,
                                  Model model){
        errorModel = model;

        if (numberOfMoney <= 0){
            errorModel.addAttribute("moneyShouldBePositive", true);
            return "redirect:/user/addMoney";
        }

        //проверка карты
        if (!card.check()) {
            errorModel.addAttribute("incorrectCard", true);
            return "redirect:/user/addMoney";
        }

        if (!card.checkMoney()){
            errorModel.addAttribute("notEnoughMoney", true);
            return "redirect:/user/addMoney";
        }

        user.setMoney(user.getMoney() + numberOfMoney);
        userService.saveUser(user);
        return null;
    }
}
