package com.att.acceptance.movie_theater.repository;

import com.att.acceptance.movie_theater.entity.Role;
import com.att.acceptance.movie_theater.entity.RoleEnum;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
    @Query("SELECT r FROM Role r WHERE r.role = :role")
    Optional<Role> findByRole(RoleEnum role);
}