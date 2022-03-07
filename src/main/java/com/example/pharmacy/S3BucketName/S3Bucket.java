package com.example.pharmacy.S3BucketName;

public enum S3Bucket {
    PROFILE_IMAGE("my-pharmacy-images");

    private final String bucketName;

    S3Bucket(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName(){
        return bucketName;
    }
}
