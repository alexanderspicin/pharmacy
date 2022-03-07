package com.example.pharmacy.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FileService {
    private final AmazonS3 s3;


    @Autowired
    public FileService(AmazonS3 s3) {
        this.s3 = s3;
    }

    public void save(String path,
                     String filename, Optional<Map<String, String>> optionalMetadata, InputStream inputStream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetadata.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(objectMetadata::addUserMetadata);
            }
        });
        try {
            s3.putObject(path, filename, inputStream, objectMetadata);
        } catch (AmazonServiceException e) {
            throw new RuntimeException("Failed to store file to s3", e);
        }
    }

    public byte[] download(String path, String key) {
        try {
            S3Object object = s3.getObject(path, key);
            return IOUtils.toByteArray(object.getObjectContent());
        } catch (AmazonServiceException | IOException e) {
            throw new RuntimeException("Failed to download file to s3", e);
        }
    }
}
