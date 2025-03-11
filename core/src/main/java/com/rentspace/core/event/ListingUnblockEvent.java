package com.rentspace.core.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListingUnblockEvent {
    private Long bookingId;
    private Long listingId;
    private String startDate;
    private String endDate;
}