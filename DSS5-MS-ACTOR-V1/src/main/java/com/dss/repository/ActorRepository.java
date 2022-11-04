package com.dss.repository;

import com.dss.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActorRepository extends JpaRepository<Actor, UUID> {
    Optional<List<Actor>> findActorsByMovieId(UUID id);
}
