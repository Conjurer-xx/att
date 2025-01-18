package com.att.acceptance.movie_theater.repository;

import com.att.acceptance.movie_theater.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}