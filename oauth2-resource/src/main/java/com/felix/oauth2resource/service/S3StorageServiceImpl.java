package com.felix.oauth2resource.service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class S3StorageServiceImpl implements StorageService {

    @Value("${storage.s3.bucket}")
    private String bucket;
    @Value("${storage.s3.region}")
    private String region;

    private AmazonS3 s3Client;

    @PostConstruct
    public void init() {
        s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(new DefaultAWSCredentialsProviderChain())
            .withRegion(region)
            .withPathStyleAccessEnabled(true)
            .withClientConfiguration(new ClientConfiguration()
                .withMaxConnections(100).withMaxErrorRetry(2))
            .build();
    }

    @Override
    public String save(MultipartFile file, String fileType) {
        String newFileName = UUID.randomUUID() + "." + fileType;
        return putS3Object(newFileName, file);
    }

    @Override
    public Resource loadAsResource(Path fullPath) throws FileNotFoundException {
        return null;
    }

    public String putS3Object(String objectKey, MultipartFile multipartFile) {
        try {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("x-amz-meta-myVal", "test");

            PutObjectRequest putObjectRequest = new PutObjectRequest(this.bucket, "zcwangtest/" + objectKey, multipartFile.getInputStream(), buildObjectMetadata(multipartFile.getSize()));
            s3Client.putObject(putObjectRequest);
            return "https://" + this.bucket + ".s3." + region + ".amazonaws.com/zcwangtest/" + objectKey;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private ObjectMetadata buildObjectMetadata(long contentLength) {
        ObjectMetadata om = new ObjectMetadata();
        om.setContentLength(contentLength);
        return om;
    }
}
