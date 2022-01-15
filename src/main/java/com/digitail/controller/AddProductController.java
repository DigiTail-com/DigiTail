package com.digitail.controller;

import com.digitail.changeColor.PictureService;
import com.digitail.model.Product;
import com.digitail.model.Status;
import com.digitail.model.User;
import com.digitail.repos.ProductRepository;
import com.digitail.repos.UserRepository;
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
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/product")
public class AddProductController {

    private ProductRepository productRepository;

    private UserRepository userRepository;

    private PictureService pictureService;

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${pictureColor.default.path}")
    private String pictureColorDefaultPath;

    @Value("${pictureColor.layers.path}")
    private String pictureColorLayersPath;

    @Autowired
    public AddProductController(ProductRepository productRepository, UserRepository userRepository, PictureService picture) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.pictureService = picture;
    }


    @GetMapping("/addProduct")
    public String addProduct(@AuthenticationPrincipal User user, Model model){
        if (user == null)
            return "redirect:/";
        model.addAttribute("product", new Product());
        model.addAttribute("user", user);

        return "product/add_product";
    }

    @PostMapping("/addProduct")
    public String addProduct(@RequestParam("file") Set<MultipartFile> files, @ModelAttribute("newProduct") @Valid Product product, @AuthenticationPrincipal User user) throws IOException, URISyntaxException {

        File folder = new File(".");
        Path uploadLayersDir = Paths.get(folder.getAbsolutePath() + pictureColorLayersPath);
        Path uploadDefaultDir = Paths.get(folder.getAbsolutePath() + pictureColorDefaultPath);

        Files.createDirectories(uploadLayersDir);
        Files.createDirectories(uploadDefaultDir);


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

            var justFile = new File(uploadLayersDir.toAbsolutePath() + "/" + resultFilename + "." + "png");
            file.transferTo(justFile);
            arrayFile.add(justFile);
        }

        pictureService.setPath(uploadDefaultDir.toAbsolutePath().toString());
        pictureService.CombineLayers(arrayFile.toArray(new File[files.size()]), uuidFile);

        product.setFileName(uuidFile);
        product.setPath("/pictureColor/default"+ "/" + uuidFile + ".png");
//        product.setName(product.getName());
        product.setUser(user);
        product.setStatus(Status.AWAITING);
        user.addProduct(product);
        productRepository.save(product);
        userRepository.saveAndFlush(user);

        return "redirect:/";
    }
}