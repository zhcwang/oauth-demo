package com.felix.oauth2resource.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public interface StorageService {

    String save(MultipartFile file, String fileType) throws IOException;

    Resource loadAsResource(Path fullPath) throws FileNotFoundException;
}
