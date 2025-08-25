package com.fattah.repository.user;

import com.fattah.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findFirstByNameEqualsIgnoreCase(String name);
}
