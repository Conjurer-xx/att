package com.att.acceptance.movie_theater.service;

import com.att.acceptance.movie_theater.entity.RoleEnum;
import com.att.acceptance.movie_theater.entity.User;
import com.att.acceptance.movie_theater.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Register a new user with a specified role.
     *
     * @param user The user to register.
     * @param role The role to assign to the user.
     * @return The registered user.
     */
    @Transactional
    public User registerUser(User user, RoleEnum role) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }

        user.getRoles().add(role);
        return userRepository.save(user);
    }

    /**
     * Fetch a user by ID.
     *
     * @param userId The user ID.
     * @return The user.
     */
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User with ID " + userId + " not found."));
    }

    /**
     * Fetch all users with pagination.
     *
     * @param pageable The pagination details.
     * @return A page of users.
     */
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * Delete a user by ID.
     *
     * @param userId The user ID.
     */
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User with ID " + userId + " does not exist."));
        userRepository.deleteById(userId);
    }
    
    /**
     * Update a user's role details.
     * 
     * @param userId The user ID.
     * @param role The role to assign to the user.
     */
    @Transactional
    public void assignRoleToUser(Long userId, RoleEnum role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        user.addRole(role);
        userRepository.save(user);
    }
    
	/**
	 * Update a user's details.
	 * 
	 * @param userId      The user ID.
	 * @param updatedUser The updated user details.
	 * @return The updated user.
	 */
	@Transactional
	public User updateUser(Long userId, User updatedUser) {
		User existingUser = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " does not exist."));

		existingUser.setName(updatedUser.getName());
		existingUser.setEmail(updatedUser.getEmail());
		existingUser.setRoles(updatedUser.getRoles());
		existingUser.setPassword(updatedUser.getPassword());

		return userRepository.save(existingUser);
	}
}