package com.digitail.controller;

import com.digitail.model.User;
import com.digitail.service.UserService;
import com.digitail.service.impl.BasketServiceImpl;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/basket")
public class BasketController {

    private final BasketServiceImpl basketService;
    private final UserService userService;

    private static Model privateModel;

    @Autowired
    public BasketController(BasketServiceImpl basketService, UserService userService) {
        this.basketService = basketService;
        this.userService = userService;
    }

    @GetMapping("")
    public String basketShow(@AuthenticationPrincipal User user
            , Model model){

        try {
            if (privateModel.containsAttribute("noCard"))
                model.addAttribute("noCard", true);
        }catch(Exception exception){
            model.addAttribute("noCard", false);
        }

        try {
            if (privateModel.containsAttribute("noMoney"))
                model.addAttribute("noMoney", true);
        }catch(Exception exception){
            model.addAttribute("noMoney", false);
        }

        try {
            if (privateModel.containsAttribute("noGoods"))
                model.addAttribute("noGoods", true);
        }catch(Exception exception){
            model.addAttribute("noGoods", false);
        }

        privateModel = null;

        var basketGoods = basketService.findAllBasketGoodsByUser(user);
        model.addAttribute("basket", basketGoods);
        model.addAttribute("user", user);
        model.addAttribute("card", user.getCard());
        return "basket/basket";
    }

    @GetMapping(value = "/delete/{id}")
    public String basketDeleteProduct(@PathVariable Long id
            , @AuthenticationPrincipal User user){

        basketService.deleteById(id);

        user.setBasketGoods(basketService.findAllBasketGoodsByUser(user));
        user.setBasketCosts(basketService.findSum(user));
        userService.saveUser(user);
        return "redirect:/basket";
    }

    @GetMapping("/download")
    public String downloadProducts(HttpSession httpSession, @AuthenticationPrincipal User user, HttpServletResponse response, Model model) {

        privateModel = model;
        if (user.getCard() == null) {
            privateModel.addAttribute("noCard", true);
            return "redirect:/basket";
        }

        if (user.getCard().checkMoney()){
            privateModel.addAttribute("noMoney", true);
            return "redirect:/basket";
        }

        if (basketService.findAllBasketGoodsByUser(user).isEmpty()){
            privateModel.addAttribute("noGoods", true);
            return "redirect:/basket";
        }

        try{
            response.addHeader("Content-Disposition", "attachment; filename=\"test.zip\"");
            var zipOutputStream = new ZipOutputStream(response.getOutputStream());

            // package files
            for (var product : basketService.findAllProductsByUser(user)) {
                var file = new File("uploads/" + product.getPath());

                //new zip entry and copying inputstream with file to zipOutputStream, after all closing streams
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                FileInputStream fileInputStream = new FileInputStream(file);

                IOUtils.copy(fileInputStream, zipOutputStream);

                fileInputStream.close();
                zipOutputStream.closeEntry();
            }
            zipOutputStream.close();
        } catch (IOException exception){
            exception.getMessage();
        }

        basketService.deleteAllByUser(user);
        user.setBasketCosts(basketService.findSum(user));
        user.setBasketGoods(basketService.findAllBasketGoodsByUser(user));
        userService.saveUser(user);

        return null;
    }
}
