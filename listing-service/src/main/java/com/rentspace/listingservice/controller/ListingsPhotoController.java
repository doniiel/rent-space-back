package com.rentspace.listingservice.controller;

import com.rentspace.listingservice.service.ListingPhotoService;
import com.rentspace.listingservice.storage.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "Photos REST API in RentSpace", description = "APIs to manage listing photos")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/listings/{listingId}/photos")
public class ListingsPhotoController {
    private final StorageService storageService;
    private final ListingPhotoService listingPhotoService;

    @Operation(summary = "Upload photos for a listing", description = "Uploads multiple photos for a listing")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Photos uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Listing not found")
    })
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'USER')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPhotos(@PathVariable @NotNull Long listingId,
                                               @RequestParam("photos") @NotEmpty List<MultipartFile> files) {
        listingPhotoService.savePhotos(listingId, files);
        return ResponseEntity.status(CREATED).body("Photos uploaded successfully.");
    }

    @Operation(summary = "Delete photos for a listing", description = "Deletes multiple photos for a listing")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Photos deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Listing not found")
    })
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'USER')")
    @DeleteMapping
    public ResponseEntity<String> deletePhotos(@PathVariable @NotNull Long listingId,
                                               @RequestParam(value = "deleteUrls", required = false) List<String> deleteUrls) {
        listingPhotoService.deletePhotos(listingId, deleteUrls != null ? deleteUrls : List.of());
        return ResponseEntity.ok("Photos deleted successfully.");
    }

    @Operation(summary = "Get a photo for a listing", description = "Retrieves a specific photo for a listing")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Photo retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Listing not found")
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER')")
    @GetMapping("/{fileName}")
    public ResponseEntity<InputStreamResource> getPhoto(@PathVariable @NotNull Long listingId,
                                                        @PathVariable @NotNull String fileName) {
        InputStream inputStream = storageService.downloadFile(fileName, "listings/" + listingId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName)
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(inputStream));
    }
}
