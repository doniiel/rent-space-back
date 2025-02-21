package com.rentspace.userservice.entity.token;

import com.rentspace.userservice.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_token")
@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetToken implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime expiryDate;
    @CreatedDate
    private LocalDateTime createdDate;
}
