package com.digitail.repos;

import com.digitail.model.Category;
import com.digitail.model.Product;
import com.digitail.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductRepo extends CrudRepository<Product, Long> {
    Set<Product> findAllByUser(User user);
    Set<Product> findAllByCategoryEquals(Category category);
}

