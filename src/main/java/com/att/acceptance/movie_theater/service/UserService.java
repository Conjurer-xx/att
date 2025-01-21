package com.att.acceptance.movie_theater.service;

import com.att.acceptance.movie_theater.entity.Role;
import com.att.acceptance.movie_theater.entity.RoleEnum;
import com.att.acceptance.movie_theater.entity.User;
import com.att.acceptance.movie_theater.exception.UserNotFoundException;
import com.att.acceptance.movie_theater.repository.RoleRepository;
import com.att.acceptance.movie_theater.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder; 

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable); 
    }

    @Transactional(readOnly = true) 
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    @Transactional
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash the password
        Role userRole = roleRepository.findByName(RoleEnum.ROLE_CUSTOMER).orElse(null); 
        user.getRoles().add(userRole); 
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found.")); 

        // Update user details (e.g., name, email)
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail()); 
        // Update password if provided
        if (userDetails.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        return userRepository.save(user); 
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}