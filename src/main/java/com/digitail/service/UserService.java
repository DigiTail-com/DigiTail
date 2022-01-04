package com.digitail.service;

import com.digitail.model.User;
import com.digitail.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public void saveUser (User user){
        userRepo.save(user);
    }

    public void editUser(User user, User editedUser){
        user.setEmail(editedUser.getEmail());
        user.setPassword(editedUser.getPassword());
        user.setFirstName(editedUser.getFirstName());
        user.setSecondName(editedUser.getSecondName());
        userRepo.saveAndFlush(user);
    }
}
