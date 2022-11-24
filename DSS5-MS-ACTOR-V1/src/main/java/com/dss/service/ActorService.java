package com.dss.service;

import com.dss.dto.ActorRequestDTO;
import com.dss.entity.Actor;

import java.util.List;
import java.util.UUID;

public interface ActorService {

    List<Actor> getActors();
    Actor getActorById(UUID id);
    Actor create(ActorRequestDTO request);
    Actor update(UUID id, ActorRequestDTO request);
    String delete(UUID id);
}
