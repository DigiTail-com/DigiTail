package com.digitail.repos;

import com.digitail.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
//    List<User> findAllByCompany(Company company);

}