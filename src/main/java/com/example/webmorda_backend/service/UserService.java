package com.example.webmorda_backend.service;

import com.example.webmorda_backend.entity.User;
import com.example.webmorda_backend.payload.RegisterRequest;
import com.example.webmorda_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;


@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    UserRepository userRepository;
    public void update(User user) {
        userRepository.save(user);
    }
    public User getUser(String login) {
        return userRepository.findUserByLogin(login);
    }

    public boolean saveUser(RegisterRequest registerRequest, User newUser) {
        boolean existsUser = userRepository.existsByLogin(registerRequest.getLogin());
        if (existsUser) {
            return false;
        }
        userRepository.save(newUser);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), authorities);
    }
}
