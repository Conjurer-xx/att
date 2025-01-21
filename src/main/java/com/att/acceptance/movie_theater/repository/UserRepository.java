package com.att.acceptance.movie_theater.repository;

import com.att.acceptance.movie_theater.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query("SELECT u FROM User u WHERE u.email = :email") 
	 Optional<User> findByEmail(String email);
}