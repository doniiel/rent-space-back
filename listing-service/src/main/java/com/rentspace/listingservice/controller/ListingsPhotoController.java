package com.rentspace.listingservice.controller;

import com.rentspace.listingservice.service.ListingPhotoService;
import com.rentspace.listingservice.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/listings/{listingId}/photos")
public class ListingsPhotoController {
    private final StorageService storageService;
    private final ListingPhotoService listingPhotoService;

    @PostMapping
    public ResponseEntity<String> uploadPhotos(@PathVariable Long listingId, @RequestParam("photos") List<MultipartFile> files) {
        listingPhotoService.savePhotos(listingId, files);
        return ResponseEntity.ok("Photos uploaded successfully.");
    }

    @DeleteMapping
    public ResponseEntity<String> deletePhotos(@PathVariable Long listingId,
                                               @RequestParam(value = "deleteUrls", required = false) List<String> deleteUrls) {
       listingPhotoService.deletePhotos(listingId, deleteUrls != null ? deleteUrls : List.of());
        return ResponseEntity.ok("Photos deleted successfully.");
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<InputStreamResource> getPhoto(@PathVariable Long listingId, @PathVariable String fileName) {
        InputStream inputStream = storageService.downloadFile(fileName, "listings/" + listingId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName)
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(inputStream));
    }
}
