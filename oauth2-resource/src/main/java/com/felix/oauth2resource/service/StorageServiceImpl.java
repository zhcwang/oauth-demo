package com.felix.oauth2resource.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

//@Service
public class StorageServiceImpl implements StorageService {
    
    @Value("${storage.location}")
    private String location;
    
    @Override
    public String save(MultipartFile file, String fileType) throws IOException {
        Path directoryPath = new File(location).toPath();
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        String newFileName = UUID.randomUUID() + "." + fileType;
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, directoryPath.resolve(newFileName),
                StandardCopyOption.REPLACE_EXISTING);
        }
        return newFileName;
    }

    @Override
    public Resource loadAsResource(Path fullPath) throws FileNotFoundException {
        try {
            Resource resource = new UrlResource(fullPath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException(
                    "Could not read file: " + fullPath);

            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not read file: " + fullPath);
        }
    }
}
