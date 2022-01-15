package com.digitail.service.impl;

import com.digitail.model.BasketGoods;
import com.digitail.model.Product;
import com.digitail.model.User;
import com.digitail.repos.BasketRepository;
import com.digitail.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class BasketServiceImpl implements IService<BasketGoods> {

    private final BasketRepository basketRepository;

    @Autowired
    public BasketServiceImpl(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    public float findSum(User user){
        var baskets = basketRepository.findAllByUser(user);
        var sum = 0.0f;
        for (var basket:baskets)
            sum += basket.getProduct().getPrice();
        return sum;
    }

    public Set<Product> findAllProductsByUser(User user){
        var baskets = basketRepository.findAllByUser(user);
        var products = new HashSet<Product>();

        for (var basket:baskets)
            products.add(basket.getProduct());

        return products;
    }

    public void save (BasketGoods basketGood){
        basketRepository.saveAndFlush(basketGood);
    }

    public Set<BasketGoods> findAllBasketGoodsByUser(User user){
        return (Set<BasketGoods>) basketRepository.findAllByUser(user);
    }

    @Override
    public Collection<BasketGoods> findAll() {
        return null;
    }

    @Override
    public Optional<BasketGoods> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public BasketGoods saveOrUpdate(BasketGoods o) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        basketRepository.deleteById(id);
    }
}
