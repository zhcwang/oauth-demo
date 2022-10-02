package com.felix.oauth2resource.service;

import com.felix.oauth2resource.model.exception.InternalServerErrorException;
import com.felix.oauth2resource.model.exception.NotImplementedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ImagePersistenceService {

    @Autowired
    private StorageService storageService;

    public String persistence(MultipartFile multipartFile) {
        String fileType = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        if (fileType == null || "".equals(fileType)) {
            String multipartFileContentType = multipartFile.getContentType();
            if (StringUtils.endsWithIgnoreCase("image/png", multipartFileContentType)) {
                fileType = "png";
            } else if (StringUtils.endsWithIgnoreCase("image/jpg", multipartFileContentType)) {
                fileType = "jpg";
            } else if (StringUtils.endsWithIgnoreCase("image/jpeg", multipartFileContentType)) {
                fileType = "jpg";
            } else {
                throw new NotImplementedException(null, "Unknown file type:" + fileType);
            }
        }
        try {
            return storageService.save(multipartFile, fileType);
        } catch (Exception e) {
            log.error("Unable to upload file.", e);
            throw new InternalServerErrorException(null, "Failed to upload file.");
        }

    }
}
