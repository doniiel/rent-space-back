package com.rentspace.listingservice.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.*;

@Table(name = "photos")
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Photos extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Listings listings;

    private String url;
}
