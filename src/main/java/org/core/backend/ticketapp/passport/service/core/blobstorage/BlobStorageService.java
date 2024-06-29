package org.core.backend.ticketapp.passport.service.core.blobstorage;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface BlobStorageService {
    String uploadFile(String folder, MultipartFile file);

    String uploadPicture(String folder, MultipartFile file);

    ResponseEntity<?> downloadAzureToBase64_(String fileName);

    ResponseEntity<?> downloadAzureToBase64(String fileName);

    void deleteFile(String fileName);
}
