package com.rentspace.listingservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.GenerationType.*;

@Table(name="listings")
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Listings extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Double pricePerNight;
    private String location;
    private Long userId;

    @OneToMany(mappedBy = "listings", cascade = CascadeType.ALL)
    private List<Photos> photos;
}
