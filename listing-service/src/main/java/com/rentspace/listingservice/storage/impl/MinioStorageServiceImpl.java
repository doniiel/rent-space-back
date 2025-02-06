package com.rentspace.listingservice.storage.impl;

import com.rentspace.listingservice.exception.StorageException;
import com.rentspace.listingservice.storage.StorageService;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MinioStorageServiceImpl implements StorageService {

    private final MinioClient minioClient;
    @Value("${minio.bucket}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file, String directory) throws StorageException {
        try (InputStream inputStream = file.getInputStream()) {
            String objectName = directory + "/" + UUID.randomUUID() +  "_" + file.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return objectName;
        } catch (Exception e) {
            throw new StorageException("Failed to upload file in Minio: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files, String directory) throws StorageException {
        return files.stream()
                .map(file -> uploadFile(file, directory))
                .collect(Collectors.toList());
    }

    @Override
    public InputStream downloadFile(String fileName, String directory) throws StorageException {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(directory + "/" + fileName)
                            .build()
            );
        } catch (Exception e) {
            throw new StorageException("Failed to download file from Minio: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteFile(String fileName, String directory) throws StorageException {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(directory + "/" + fileName)
                            .build()
            );
        } catch (Exception e) {
            throw new StorageException("Failed to delete file from Minio: " + e.getMessage(), e);
        }
    }
}

