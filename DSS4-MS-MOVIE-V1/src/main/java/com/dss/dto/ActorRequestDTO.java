package com.dss.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorRequestDTO {
    private String firstName;
    private String lastName;
    private String gender;
    private int age;
    private UUID movieId;
}
