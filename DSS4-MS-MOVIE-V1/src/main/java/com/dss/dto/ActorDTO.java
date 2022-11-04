package com.dss.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorDTO {
    private UUID actorId;
    private String firstName;
    private String lastName;
    private String gender;
    private int age;
    private UUID movieId;
}
