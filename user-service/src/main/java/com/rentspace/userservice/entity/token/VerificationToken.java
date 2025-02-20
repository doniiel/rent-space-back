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

import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "verification_tokens")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String token;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime expiryDate;
    @CreatedDate
    private LocalDateTime createdDate;
}
