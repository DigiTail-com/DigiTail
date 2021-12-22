package com.digitail.controller;

import com.digitail.changeColor.PictureService;
import com.digitail.model.Category;
import com.digitail.repos.ProductRepo;
import com.digitail.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
        return "product/show_products";
    }

    @GetMapping("/download")
    public ResponseEntity<Object> downloadFile() throws FileNotFoundException {
        String fileName = "/Users/daniil/Desktop/DigiTail/src/main/resources/static/uploads/pictureColor/default/7b57192b-b13e-4518-81d1-21a5a76b2976.png";
        var file = new File(fileName);
        var resources = new InputStreamResource(new FileInputStream(file));
        var headers = new HttpHeaders();
        headers.add("Content-Disposition",
                String.format("attachment; filename=\"%s\"", file.getName()));
        headers.add("Cache-Control", "no-cache, no_store, must-revalidate");
        headers.add("Pragma","no-cache");
        headers.add("Expires", "0");

        ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.IMAGE_PNG).body(resources);
        return responseEntity;
    }
}
