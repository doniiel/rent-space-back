package com.rentspace.userservice.entity.token;

import com.rentspace.userservice.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "verification_tokens", indexes = {
        @Index(name = "idx_verification_token", columnList = "token"),
        @Index(name = "idx_verification_user_id", columnList = "user_id")
})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class VerificationToken implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Transient
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
