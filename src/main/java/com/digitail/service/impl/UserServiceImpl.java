package com.digitail.service.impl;

import com.digitail.model.User;
import com.digitail.repos.UserRepo;
import com.digitail.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl implements IService<User> {

    private final UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public Collection<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public User saveOrUpdate(User user) {
        return userRepo.saveAndFlush(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }

    public User findUserByUsername(String username){
        return userRepo.findByUsername(username);
    }
}
