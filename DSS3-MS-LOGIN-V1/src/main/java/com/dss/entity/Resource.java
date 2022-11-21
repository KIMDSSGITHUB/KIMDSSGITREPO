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
@Table(name = "resource")
public class Resource {

    @Id
    @GeneratedValue
    @Column(name = "id")
    @Type(type = "pg-uuid")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    private Permission permission;

    @JsonManagedReference
    @OneToMany(mappedBy = "resource",cascade = CascadeType.ALL)
    private Set<Action> actions;
}
