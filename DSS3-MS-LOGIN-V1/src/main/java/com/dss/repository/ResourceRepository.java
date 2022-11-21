package com.dss.repository;

import com.dss.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResourceRepository extends JpaRepository<Resource, UUID> {
}
