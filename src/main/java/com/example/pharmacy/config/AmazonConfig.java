package com.example.pharmacy.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig {

    @Bean
    public AmazonS3 s3(){
        AWSCredentials awsCredentials = new BasicAWSCredentials(
                "AKIAQP6DWXLKJFLUSS5L",
                "87SHt2ZhU+zowUTtRIao2NYdsz8oUAIh1mdz8hzs"
        );
        return AmazonS3ClientBuilder
                .standard()
                .withRegion("eu-west-1")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
        }
    }

