package org.core.backend.ticketapp.passport.service.core.blobstorage;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BlobStorageServiceImpl implements BlobStorageService {
    @Override
    public String uploadFile(String folder, MultipartFile file) {
        return "";
    }

    @Override
    public String uploadPicture(String folder, MultipartFile file) {
        return "";
    }

    @Override
    public ResponseEntity<?> downloadAzureToBase64_(String fileName) {
        return null;
    }

    @Override
    public ResponseEntity<?> downloadAzureToBase64(String fileName) {
        return null;
    }

    @Override
    public void deleteFile(String fileName) {

    }
}
