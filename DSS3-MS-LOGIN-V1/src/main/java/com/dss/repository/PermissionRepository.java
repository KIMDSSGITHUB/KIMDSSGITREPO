package com.dss.repository;

import com.dss.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission,UUID> {
}
