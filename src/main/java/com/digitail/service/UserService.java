package com.digitail.service;

import com.digitail.model.User;
import com.digitail.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public void saveUser (User user){
        userRepository.save(user);
    }

    public void editUser(User user, User editedUser){
        user.setEmail(editedUser.getEmail());
        user.setPassword(editedUser.getPassword());
        user.setFirstName(editedUser.getFirstName());
        user.setSecondName(editedUser.getSecondName());
        userRepository.saveAndFlush(user);
    }
}
