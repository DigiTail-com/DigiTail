package com.digitail.controller;

import com.digitail.changeColor.PictureService;
import com.digitail.model.Category;
import com.digitail.repos.ProductRepo;
import com.digitail.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ShowProductsController {
    private ProductRepo productRepo;

    private UserRepo userRepo;

    private PictureService pictureService;

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${pictureColor.default.path}")
    private String pictureColorDefaultPath;

    @Value("${pictureColor.layers.path}")
    private String pictureColorLayersPath;

    @Autowired
    public ShowProductsController(ProductRepo productRepo, UserRepo userRepo, PictureService picture) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.pictureService = picture;
    }

    @GetMapping("/showProducts")
    public String showProduct(Model model){
        var products = productRepo.findAllByCategoryEquals(Category.PICTURE_COLOR);
        model.addAttribute("products", products);
        return "productPage";
    }
}
