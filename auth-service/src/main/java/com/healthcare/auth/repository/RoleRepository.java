package com.healthcare.auth.repository;

import com.healthcare.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    
    Optional<Role> findByRoleName(String roleName);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.role.roleId = :roleId")
    boolean isRoleInUse(@Param("roleId") UUID roleId);
}