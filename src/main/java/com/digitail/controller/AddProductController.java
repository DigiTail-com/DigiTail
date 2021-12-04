package com.digitail.controller;

import com.digitail.model.Product;
import com.digitail.model.User;
import com.digitail.repos.ProductRepo;
import com.digitail.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/product")
public class AddProductController {

    private ProductRepo productRepo;

    private UserRepo userRepo;

    @Autowired
    public AddProductController(ProductRepo productRepo, UserRepo userRepo) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/addProduct")
    public String addProduct(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("newProduct", new Product());

        return "addProduct";
    }

    @PostMapping("/addProduct")
    public String addProduct(@RequestParam("file") MultipartFile file, @ModelAttribute("newProduct") @Valid Product product, @AuthenticationPrincipal User user) throws IOException {

        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + file.getOriginalFilename();

        file.transferTo(new File(uploadPath + "/" + resultFilename));

        product.setFileName(resultFilename);

        var user1 = userRepo.findByUsername(user.getUsername());


        product.setUser(user1);
        user1.addProduct(product);
        productRepo.save(product);

        userRepo.saveAndFlush(user1);

        return null;
    }
}
