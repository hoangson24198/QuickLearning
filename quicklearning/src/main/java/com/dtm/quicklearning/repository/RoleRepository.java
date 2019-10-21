package com.dtm.quicklearning.repository;

import com.dtm.quicklearning.model.eNum.RoleName;
import com.dtm.quicklearning.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName roleName);
}
