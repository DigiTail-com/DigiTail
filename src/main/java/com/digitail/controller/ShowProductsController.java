package com.digitail.controller;

import com.digitail.changeColor.PictureService;
import com.digitail.model.Category;
import com.digitail.model.Product;
import com.digitail.model.Status;
import com.digitail.model.User;
import com.digitail.repos.ProductRepo;
import com.digitail.repos.UserRepo;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    public String showProduct(Model model, @AuthenticationPrincipal User user){
        if (user == null)
            return "redirect:/";
        var products = productRepo.findAllByCategoryEqualsAndStatusEquals(Category.PICTURE_COLOR, Status.APPROVED);

        model.addAttribute("products", products);
        model.addAttribute("flag", Category.PICTURE_COLOR.name());
        return "product/show_products";
    }

    @PostMapping("/showProducts")
    public String sortingProducts(@Valid String category, Model model){
        var products = productRepo.findAllByCategoryEqualsAndStatusEquals(Category.valueOf(category), Status.APPROVED);

        model.addAttribute("flag", category);
        model.addAttribute("products", products);
        return "product/show_products";
    }

    @GetMapping("/showProducts/{id}")
    public String showProduct(@PathVariable("id") long id, Model model, @AuthenticationPrincipal User user){
        if (user == null)
            return "redirect:/";

        model.addAttribute("product", productRepo.findById(id));
        return "product/product_page";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Object> downloadFile(@PathVariable("id") long id, @AuthenticationPrincipal User user) throws FileNotFoundException {
        var product = productRepo.findById(id);
        String fileName = product.getPath();
        var file = new File("uploads/" + fileName);
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

    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> downloadProducts(@AuthenticationPrincipal User user) throws FileNotFoundException {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"test.zip\"")
                .body(out -> {
                    var zipOutputStream = new ZipOutputStream(out);

                    // package files
                    for (var product : productRepo.findAll()) {
                        var file = new File("uploads/" + product.getPath());

                        //new zip entry and copying inputstream with file to zipOutputStream, after all closing streams
                        zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                        FileInputStream fileInputStream = new FileInputStream(file);

                        IOUtils.copy(fileInputStream, zipOutputStream);

                        fileInputStream.close();
                        zipOutputStream.closeEntry();
                    }

                    zipOutputStream.close();
                });
    }
}
