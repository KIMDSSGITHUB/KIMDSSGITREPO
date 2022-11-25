package com.dss.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "actor")
public class Actor {
    @Id
    @Column(name = "actor_id")
    @Type(type = "pg-uuid")
    private UUID actorId;
    @Column(name = "first_name",nullable = false)
    private String firstName;
    @Column(name = "last_name",nullable = false)
    private String lastName;
    @Column(name = "gender",nullable = false)
    private String gender;
    @Column(name = "age",nullable = false)
    private int age;
    @JsonIgnore
    @ManyToMany(mappedBy = "actors")
    private Set<Movie> movies = new HashSet<>();

}
