package com.rentspace.notificationservice.entity;

import com.rentspace.notificationservice.enums.NotificationStatus;
import com.rentspace.notificationservice.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "notifications")
@Getter @Setter
@Builder @ToString
@AllArgsConstructor
@NoArgsConstructor
public class Notifications implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String email;

    @Enumerated(STRING)
    private NotificationType type;

    private String message;

    @Enumerated(STRING)
    private NotificationStatus status;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
