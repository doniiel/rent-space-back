package com.rentspace.listingservice.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface StorageService {
    String uploadFile(MultipartFile file, String directory) throws StorageException;
    List<String> uploadFiles(List<MultipartFile> files, String directory) throws StorageException;
    InputStream downloadFile(String fileName, String directory) throws StorageException;
    void deleteFile(String fileName, String directory) throws StorageException;
}
