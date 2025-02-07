package com.rentspace.listingservice.storage;

import com.rentspace.listingservice.exception.StorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface StorageService {
    String uploadFile(MultipartFile file, String directory);
    List<String> uploadFiles(List<MultipartFile> files, String directory);
    String getFileUrl(String fileName, String directory);
    InputStream downloadFile(String fileName, String directory);
    void deleteFile(String fileName, String directory);
}
