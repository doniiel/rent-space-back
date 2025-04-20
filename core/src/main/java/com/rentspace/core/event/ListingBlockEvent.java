package com.rentspace.core.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListingBlockEvent {
    private Long bookingId;
    private Long listingId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
