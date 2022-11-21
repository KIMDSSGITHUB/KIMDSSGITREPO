package com.dss.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "permission")
public class Permission {

    @Id
    @GeneratedValue
    @Column(name = "id")
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "name")
    private String name;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY,targetEntity= Role.class)
    @JoinColumn(name = "role_id")
    private Role role;

    @JsonManagedReference
    @OneToMany(mappedBy = "permission",cascade = CascadeType.ALL)
    private Set<Resource> resources;

}
