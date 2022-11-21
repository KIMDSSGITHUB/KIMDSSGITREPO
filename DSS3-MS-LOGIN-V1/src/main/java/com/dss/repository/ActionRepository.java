package com.dss.repository;

import com.dss.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActionRepository extends JpaRepository<Action, UUID> {
}
