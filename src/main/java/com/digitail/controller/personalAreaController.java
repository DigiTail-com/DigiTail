package com.digitail.controller;

import com.digitail.model.Product;
import com.digitail.model.Status;
import com.digitail.model.User;
import com.digitail.repos.ProductRepo;
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
    private ProductRepo productRepo;

    @Autowired
    public personalAreaController(UserService userService, ProductRepo productRepo) {
        this.userService = userService;
        this.productRepo = productRepo;
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
        var products = productRepo.findAllByUser(user);
        model.addAttribute("products", products);
        return "product/user_products";
    }

    @GetMapping("/showProduct/{id}")
    public String showUserProduct(@PathVariable long id, @AuthenticationPrincipal User user, Model model){
        model.addAttribute("product", productRepo.findById(id));
        return "product/user_product_page";
    }

    @PostMapping("/editProduct/{id}")
    public String editUserProduct(@PathVariable long id,
                                  @ModelAttribute("product") @Valid Product product,
                                  @AuthenticationPrincipal User user){
        var productFromDB = productRepo.findById(id);
        productFromDB.setStatus(Status.AWAITING);
        productFromDB.setName(product.getName());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setCategory(product.getCategory());

        productRepo.save(productFromDB);

        return "redirect:/user/showProduct/" + id;
    }
}
