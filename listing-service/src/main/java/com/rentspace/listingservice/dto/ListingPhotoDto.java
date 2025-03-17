package com.rentspace.listingservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Schema(description = "Request object for uploading a photo to a listing")
public class ListingPhotoDto {

    @Schema(description = "Unique identifier of the photo", example = "1", required = false)
    private Long id;

    @Schema(description = "URL of the uploaded photo (populated after upload)", example = "http://example.com/photo.jpg", required = false)
    private String photoUrl;

    @NotNull(message = "File cannot be null.")
    @Schema(description = "File to be uploaded as a photo", required = true)
    private MultipartFile file;

    @AssertTrue(message = "File cannot be empty.")
    @Schema(hidden = true)
    public boolean isFileNotEmpty() {
        return file != null && !file.isEmpty();
    }
}