package com.dss.controller;

import com.dss.dto.ActorRequestDTO;
import com.dss.entity.Actor;
import com.dss.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/actors")
public class ActorController {

    @Autowired
    private ActorService actorService;


    @GetMapping()
    public ResponseEntity<List<Actor>> getActors() {
        return new ResponseEntity<>(actorService.getActors(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actor> getActor(@PathVariable UUID id) {
        return new ResponseEntity<>(actorService.getActorById(id), HttpStatus.OK);
    }


    @PostMapping()
    public ResponseEntity<Actor> create(@RequestBody ActorRequestDTO request) {
        return new ResponseEntity<>(actorService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Actor> update(@PathVariable UUID id, @RequestBody ActorRequestDTO newRequest) {
        return new ResponseEntity<>(actorService.update(id, newRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(actorService.delete(id),HttpStatus.OK);
    }

}
