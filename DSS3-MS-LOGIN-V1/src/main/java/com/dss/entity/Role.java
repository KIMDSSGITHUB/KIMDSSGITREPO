package com.dss.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue
    @Column(name = "id")
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name ="name")
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, targetEntity= Permission.class)
    private Set<Permission> permissions;
}
