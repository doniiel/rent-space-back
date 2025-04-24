package com.rentspace.core.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListingNotificationEvent {
    private Long userId;
    private Long listingId;
    private String message;
    private String eventType;
}
