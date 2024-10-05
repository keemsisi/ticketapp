package org.core.backend.ticketapp.passport.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.core.backend.ticketapp.common.dto.GenericResponse;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.passport.dtos.core.FileTemplateDto;
import org.core.backend.ticketapp.passport.entity.FileTemplate;
import org.core.backend.ticketapp.passport.service.core.FileTemplateService;
import org.core.backend.ticketapp.passport.service.core.blobstorage.BlobStorageService;
import org.core.backend.ticketapp.passport.util.ActivityLogProcessorUtils;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping
public class FileStorageController {

    @Autowired
    private BlobStorageService storageService;

    @Autowired
    private FileTemplateService fileTemplateService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private ActivityLogProcessorUtils activityLogProcessorUtils;
    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(value = "/api/v1/uploads", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file, @RequestPart @NotNull String folder,
                                    @RequestParam(required = false, defaultValue = "false") Boolean isImage) throws Exception {
        String url;
        if (!isImage) url = storageService.uploadFile(folder, file);
        else url = storageService.uploadPicture(folder, file);
        Map<String, Object> map = Map.of("originalFileName", Objects.requireNonNull(file.getOriginalFilename()),
                "name", file.getName(),
                "contentType", Objects.requireNonNull(file.getContentType()),
                "size", file.getSize(),
                "folder", folder
        );
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), FileTemplate.class.getTypeName(), null, objectMapper.writeValueAsString(map), "Initiated a request to upload a file into a folder");
        return new ResponseEntity<>(new GenericResponse<>("00", "File uploaded successfully", url), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/v1/uploads", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<?> download(@RequestParam @NotNull String fileName, @RequestParam(defaultValue = "byte", required = false) String type) throws IOException {
        if (type.equalsIgnoreCase("file"))
            return storageService.downloadAzureToBase64_(fileName);
        return storageService.downloadAzureToBase64(fileName);
    }

    @RequestMapping(value = "/api/v1/uploads", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteFile(@RequestParam @NotNull String fileName) {
        storageService.deleteFile(fileName);
        return new ResponseEntity<>(new GenericResponse<>("00", "File deleted successfully", null), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/v1/templates", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadTemplate(@RequestPart("file") MultipartFile file, @Validated FileTemplateDto template) throws Exception {
        try {
            var user = jwtTokenUtil.getUser();
            var url = storageService.uploadFile("templates", file);
            template.setUrl(url);
            fileTemplateService.create(template, user);
            return new ResponseEntity<>(new GenericResponse<>("00", "File uploaded successfully", url), HttpStatus.OK);
        } finally {
            Map<String, Object> map = Map.of("originalFileName", Objects.requireNonNull(file.getOriginalFilename()),
                    "name", file.getName(),
                    "contentType", Objects.requireNonNull(file.getContentType()),
                    "size", file.getSize(),
                    "fileTemplateDto", template
            );
            activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), FileTemplate.class.getTypeName(), null, objectMapper.writeValueAsString(map), "Initiated a request to upload a template");
        }
    }

    @RequestMapping(value = "/api/v1/templates", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTemplate(@RequestPart("file") MultipartFile file, @Validated FileTemplateDto template) throws Exception {
        var url = storageService.uploadPicture("templates", file);
        var existingTemplate = fileTemplateService.getTemplateByName(template.getName());
        try {
            url = storageService.uploadPicture("templates", file);
            existingTemplate = fileTemplateService.getTemplateByName(template.getName());
            if (existingTemplate.isEmpty())
                throw new ApplicationException(400, "invalid_request", "Template with specified name doesn't exist.");
            existingTemplate.get().setUrl(url);
            fileTemplateService.save(existingTemplate.get());
            return new ResponseEntity<>(new GenericResponse<>("00", "File updated successfully", url), HttpStatus.OK);
        } finally {
            activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), FileTemplate.class.getTypeName(), null, null, "Initiated a request to update a template");
        }
    }

    @RequestMapping(value = "/api/v1/templates", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTemplateByName(@RequestParam String name) {
        return new ResponseEntity<>(new GenericResponse<>("00", "Template retrieved successfully", fileTemplateService.getTemplateByName(name)), HttpStatus.OK);
    }
}