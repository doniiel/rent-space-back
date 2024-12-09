package com.rentspace.listingservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PhotoDto {

    private Long id;
    private String url;

    @NotNull(message = "File cannot be null.")
    @NotEmpty(message = "File cannot be empty.")
    private MultipartFile file;
}
