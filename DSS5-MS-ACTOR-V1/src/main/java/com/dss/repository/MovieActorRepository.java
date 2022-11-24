package com.dss.repository;

import com.dss.entity.MovieActor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MovieActorRepository extends JpaRepository<MovieActor, UUID> {
    List<MovieActor> findByActorId(UUID actorId);
}
