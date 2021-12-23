package com.digitail.controller;

import antlr.collections.List;
import com.digitail.model.Product;
import com.digitail.model.Status;
import com.digitail.model.User;
import com.digitail.repos.ProductRepo;
import com.digitail.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;


@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController{

    private UserRepo userRepo;
    private ProductRepo productRepo;

    @Autowired
    public AdminController(UserRepo userRepo, ProductRepo productRepo) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    @GetMapping()
    public String UsersList(Model model){
        model.addAttribute("users", userRepo.findAll());
        return "userList";
    }

    @GetMapping("/products")
    public String allProducts(Model model){
        model.addAttribute("products", productRepo.findAll());
        model.addAttribute("flag", Status.DEFAULT.name());
        return "admin/show_products_for_admin";
    }

    @GetMapping("/products/{id}")
    public String showProduct(@PathVariable("id") long id, Model model){
        model.addAttribute("product", productRepo.findById(id));
        return "admin/admin_product_page";
    }

    @PostMapping("/editProduct/{id}")
    public String editProduct(@PathVariable("id") long id, @ModelAttribute("product") @Valid Product product){
        var productFromDB = productRepo.findById(id);
        productFromDB.setStatus(product.getStatus());
        productFromDB.setName(product.getName());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setCategory(product.getCategory());

        productRepo.save(productFromDB);

        return "redirect:/admin/products/" + id;
    }

    @PostMapping("/products")
    public String sortingProducts(@Valid String status, Model model){
        var allProducts = productRepo.findAll();
        Set<Product> products = new HashSet<Product>();
        if (status.equals("Default"))
            return "redirect:/admin/products";

        for (var qw: allProducts) {
            if (qw.getStatus().name().equals(status))
                products.add(qw);
        }
        model.addAttribute("flag", status);
        model.addAttribute("products", products);
        return "admin/show_products_for_admin";
    }
}
