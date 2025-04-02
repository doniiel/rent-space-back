package com.rentspace.userservice.entity.user;

import com.rentspace.userservice.enums.Currency;
import com.rentspace.userservice.enums.Language;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "user_profiles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserProfiles implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 500)
    private String bio;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Enumerated(STRING)
    @Column(nullable = false, length = 100)
    private Language language;

    @Enumerated(STRING)
    @Column(nullable = false, length = 100)
    private Currency currency;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
