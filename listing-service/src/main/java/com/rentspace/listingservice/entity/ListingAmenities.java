package com.rentspace.listingservice.entity;

import com.rentspace.listingservice.enums.AmenityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "listing_amenities", indexes = {
        @Index(name = "idx_listing_availability_listing_id", columnList = "listing_id")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ListingAmenities implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;

    @ElementCollection(targetClass = AmenityType.class)
    @Enumerated(STRING)
    @CollectionTable(name = "listing_amenity_types", joinColumns = @JoinColumn(name = "listing_amenity_id"))
    @Column(name = "amenity_type")
    private Set<AmenityType> amenityTypes = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "ListingAmenities{id=" + id + ", amenityTypes=" + amenityTypes + "}";
    }
}
