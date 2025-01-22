package com.att.acceptance.movie_theater.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.att.acceptance.movie_theater.entity.RoleEnum;
import com.att.acceptance.movie_theater.entity.User;


/**
 * Repository for accessing User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    

    /**
     * Find users by a specific role.
     *
     * @param role The role to search for.
     * @return A list of users with the given role.
     */
    @Query("SELECT u FROM User u WHERE :role MEMBER OF u.roles")
    List<User> findByRole(@Param("role") RoleEnum role);
}

