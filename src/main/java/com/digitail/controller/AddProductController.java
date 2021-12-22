package com.digitail.controller;

import com.digitail.changeColor.PictureService;
import com.digitail.model.Category;
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

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/product")
public class AddProductController {

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
    public AddProductController(ProductRepo productRepo, UserRepo userRepo, PictureService picture) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.pictureService = picture;
    }


    @GetMapping("/addProduct")
    public String addProduct(@AuthenticationPrincipal User user, Model model){
        if (user == null)
            return "redirect:/";
        model.addAttribute("product", new Product());
//        model.addAttribute("user", user);

        return "product/add_product";
    }

    @PostMapping("/addProduct")
    public String addProduct(@RequestParam("file") Set<MultipartFile> files, @ModelAttribute("newProduct") @Valid Product product, @AuthenticationPrincipal User user) throws IOException {

        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        var uuidFile = UUID.randomUUID().toString();

        var counter = 0;
        var arrayFile = new ArrayList<File>();
        for (var file:files) {
            var resultFilename = new String();
            if (file.getOriginalFilename().contains("c."))
                resultFilename = uuidFile + "_" + counter + "c";
            else resultFilename = uuidFile + "_" + counter;

            var contentType = file.getContentType();
            counter++;

            var justFile = new File(pictureColorLayersPath + "/" + resultFilename + "." + "png");
            file.transferTo(justFile);
            arrayFile.add(justFile);
        }

        pictureService.setPath(pictureColorDefaultPath);
        pictureService.CombineLayers(arrayFile.toArray(new File[files.size()]), uuidFile);

        product.setFileName(uuidFile);
        product.setPath("/uploads/pictureColor/default" + "/" + uuidFile + ".png");
//        product.setName(product.getName());
        product.setCategory(Category.PICTURE_COLOR);
        product.setUser(user);
        user.addProduct(product);
        productRepo.save(product);
        userRepo.saveAndFlush(user);

        return "redirect:/";
    }
}