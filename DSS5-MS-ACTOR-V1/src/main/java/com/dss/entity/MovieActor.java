package com.dss.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movieActors")
public class MovieActor {

    @Id
    @Column(name = "movie_id")
    private UUID movieId;
    @Column(name = "actor_id")
    private UUID actorId;
}
