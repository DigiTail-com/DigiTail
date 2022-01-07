package com.digitail.controller;

import com.digitail.model.Product;
import com.digitail.model.Status;
import com.digitail.repos.ProductRepository;
import com.digitail.repos.UserRepository;
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

    private UserRepository userRepository;
    private ProductRepository productRepository;

    @Autowired
    public AdminController(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @GetMapping()
    public String UsersList(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "userList";
    }

    @GetMapping("/products")
    public String allProducts(Model model){
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("flag", Status.DEFAULT.name());
        return "admin/show_products_for_admin";
    }

    @GetMapping("/products/{id}")
    public String showProduct(@PathVariable("id") long id, Model model){
        model.addAttribute("product", productRepository.findById(id));
        return "admin/admin_product_page";
    }

    @PostMapping("/editProduct/{id}")
    public String editProduct(@PathVariable("id") long id, @ModelAttribute("product") @Valid Product product){
        var productFromDB = productRepository.findById(id);
        productFromDB.setStatus(product.getStatus());
        productFromDB.setName(product.getName());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setCategory(product.getCategory());

        productRepository.save(productFromDB);

        return "redirect:/admin/products/" + id;
    }

    @PostMapping("/products")
    public String sortingProducts(@Valid String status, Model model){
        var allProducts = productRepository.findAll();
        Set<Product> products = new HashSet<Product>();

        if (Status.DEFAULT.name().equals(status))
            model.addAttribute("products", productRepository.findAll());
        else {
            for (var qw : allProducts) {
                if (qw.getStatus().name().equals(status))
                    products.add(qw);
            }
            model.addAttribute("products", products);
        }

        model.addAttribute("flag", status);
        return "admin/show_products_for_admin";
    }
}
