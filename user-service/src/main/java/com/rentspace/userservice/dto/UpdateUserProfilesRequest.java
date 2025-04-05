package com.rentspace.userservice.dto;

import com.rentspace.core.enums.Currency;
import com.rentspace.core.enums.Language;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserProfilesRequest {
    private String bio;
    private String avatarUrl;

    @NotNull(message = "Language cannot be null")
    private Language language;

    @NotNull(message = "Currency cannot be null")
    private Currency currency;
}
