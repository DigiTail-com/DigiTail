package com.digitail.repos;

import com.digitail.model.BasketGoods;
import com.digitail.model.Product;
import com.digitail.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketRepository extends JpaRepository<BasketGoods, Long> {
    List<BasketGoods> findAllByUser (User user);
}
