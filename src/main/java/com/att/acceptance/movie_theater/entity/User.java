package com.att.acceptance.movie_theater.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entity representing a User in the movie theater system.
 * Each user has roles that determine their access permissions.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the user.")
    private Long id;

    /**
     * The full name of the user.
     */
    @NotBlank(message = "Name is required.")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Column(nullable = false)
    @Schema(description = "The full name of the user.")
    private String name;

    /**
     * The email address of the user. This must be unique and valid.
     */
    @Email(message = "Invalid email format.")
    @NotBlank(message = "Email is required.")
    @Column(nullable = false, unique = true)
    @Schema(description = "The email address of the user.")
    private String email;

    /**
     * The password for the user account.
     */
    @NotBlank(message = "Password is required.")
    @Column(nullable = false)
    @Schema(description = "The password for the user account.")
    private String password;

    /**
     * The roles assigned to the user, determining their access permissions.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )    
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Schema(description = "The roles assigned to the user.")
    private Set<RoleEnum> roles = new HashSet<>();
    
    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void addRole(RoleEnum role) {
        roles.add(role);
    }

    public void removeRole(RoleEnum role) {
        roles.remove(role);
    }

    public void clearRoles() {
        roles.clear();
    }


    /**
	 * @return the roles
	 */
	public Set<RoleEnum> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(Set<RoleEnum> roles) {
		this.roles = roles;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", roles=" + roles
				+ "]";
	}


}