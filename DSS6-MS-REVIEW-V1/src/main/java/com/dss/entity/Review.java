package com.dss.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "review_id")
    @Type(type = "pg-uuid")
    private UUID reviewId;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "posted_date")
    @CreationTimestamp
    private Instant postedDate;

    @Column(name = "rating")
    private int rating;

    @ManyToOne
    @JoinColumn(name="movie_id", referencedColumnName = "movie_Id")
    private Movie movie;
}
