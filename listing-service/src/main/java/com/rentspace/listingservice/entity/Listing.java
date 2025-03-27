package com.rentspace.listingservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rentspace.listingservice.enums.ListingType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="listing", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Listing {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    @NotNull
    private String title;

    @Column(length = 500)
    @NotNull
    private String description;

    @Column(nullable = false)
    @NotNull
    private String address;

    @Column(nullable = false, length = 100)
    @NotNull
    private String city;

    @Column(nullable = false, length = 100)
    @NotNull
    private String country;

    private BigDecimal latitude;
    private BigDecimal longitude;

    @Enumerated(STRING)
    @Column(nullable = false)
    private ListingType type;

    @Column(name = "max_guests")
    @Min(1)
    private Integer maxGuests;

    @Column(name = "price_per_night", nullable = false)
    @NotNull
    @DecimalMin("1.00")
    private Double pricePerNight;

    @Column(name = "average_rating", nullable = false, columnDefinition = "FLOAT default 0")
    private Double averageRating;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private String updatedBy;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 10)
    @JsonIgnore
    private List<ListingPhoto> photos;
}
