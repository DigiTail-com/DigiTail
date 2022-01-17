package com.digitail.repos;

import com.digitail.model.BasketGoods;
import com.digitail.model.Product;
import com.digitail.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BasketRepository extends JpaRepository<BasketGoods, Long> {
    Set<BasketGoods> findAllByUser (User user);
    void deleteById(long id);
}
