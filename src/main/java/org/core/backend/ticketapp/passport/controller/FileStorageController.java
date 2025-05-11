package org.core.backend.ticketapp.passport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.core.backend.ticketapp.common.GenericResponse;
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

/**
 * The FileStorageController is responsible for handling file uploads, downloads, deletions, and management of file templates.
 * It also processes activity logs to track file-related actions and provides endpoints for handling file templates.
 */
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

    /**
     * Uploads a file to a specified folder.
     *
     * This method handles the upload of a file, either as a general file or an image. It logs the activity and returns
     * a URL where the file can be accessed.
     *
     * @param file     the file to be uploaded.
     * @param folder   the folder where the file will be uploaded.
     * @param isImage  a flag indicating whether the file is an image (default is false).
     * @return a response entity with a success message and the URL of the uploaded file.
     * @throws Exception if an error occurs during the file upload process.
     */
    @RequestMapping(value = "/api/v1/uploads", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file, @RequestPart @NotNull String folder,
                                    @RequestParam(required = false, defaultValue = "false") Boolean isImage) throws Exception {
        String url;
        if (!isImage) url = storageService.uploadFile(folder, file);
        else url = storageService.uploadPicture(folder, file);

        // Prepare the log data for file upload activity
        Map<String, Object> map = Map.of("originalFileName", Objects.requireNonNull(file.getOriginalFilename()),
                "name", file.getName(),
                "contentType", Objects.requireNonNull(file.getContentType()),
                "size", file.getSize(),
                "folder", folder
        );
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), FileTemplate.class.getTypeName(), null, objectMapper.writeValueAsString(map), "Initiated a request to upload a file into a folder");

        return new ResponseEntity<>(new GenericResponse<>("00", "File uploaded successfully", url), HttpStatus.OK);
    }

    /**
     * Downloads a file from the storage service.
     *
     * This method allows for downloading a file by its name, and optionally converts it to base64 based on the specified type.
     *
     * @param fileName the name of the file to be downloaded.
     * @param type     the type of download (either "file" for raw download or "byte" for base64 conversion).
     * @return a response entity containing the file data.
     * @throws IOException if an error occurs during the file download process.
     */
    @RequestMapping(value = "/api/v1/uploads", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<?> download(@RequestParam @NotNull String fileName, @RequestParam(defaultValue = "byte", required = false) String type) throws IOException {
        if (type.equalsIgnoreCase("file"))
            return storageService.downloadAzureToBase64_(fileName);
        return storageService.downloadAzureToBase64(fileName);
    }

    /**
     * Deletes a file from the storage service.
     *
     * This method deletes the specified file from the storage service and logs the activity.
     *
     * @param fileName the name of the file to be deleted.
     * @return a response entity with a success message.
     */
    @RequestMapping(value = "/api/v1/uploads", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteFile(@RequestParam @NotNull String fileName) {
        storageService.deleteFile(fileName);
        return new ResponseEntity<>(new GenericResponse<>("00", "File deleted successfully", null), HttpStatus.OK);
    }

    /**
     * Uploads a file template.
     *
     * This method uploads a file template, saves its URL in the database, and logs the activity. It is used for managing
     * templates for later use.
     *
     * @param file     the file template to be uploaded.
     * @param template the file template DTO containing metadata for the template.
     * @return a response entity with a success message and the URL of the uploaded template.
     * @throws Exception if an error occurs during the file upload or template creation process.
     */
    @RequestMapping(value = "/api/v1/templates", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadTemplate(@RequestPart("file") MultipartFile file, @Validated FileTemplateDto template) throws Exception {
        try {
            var user = jwtTokenUtil.getUser();
            var url = storageService.uploadFile("templates", file);
            template.setUrl(url);
            fileTemplateService.create(template, user);
            return new ResponseEntity<>(new GenericResponse<>("00", "File uploaded successfully", url), HttpStatus.OK);
        } finally {
            // Log the file upload activity
            Map<String, Object> map = Map.of("originalFileName", Objects.requireNonNull(file.getOriginalFilename()),
                    "name", file.getName(),
                    "contentType", Objects.requireNonNull(file.getContentType()),
                    "size", file.getSize(),
                    "fileTemplateDto", template
            );
            activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), FileTemplate.class.getTypeName(), null, objectMapper.writeValueAsString(map), "Initiated a request to upload a template");
        }
    }

    /**
     * Updates an existing file template.
     *
     * This method allows updating an existing file template by uploading a new file. It checks if the template exists
     * and updates the URL in the database. Activity is logged.
     *
     * @param file     the new file to be uploaded as part of the template update.
     * @param template the updated file template DTO.
     * @return a response entity with a success message and the URL of the updated template.
     * @throws Exception if an error occurs during the template update process.
     */
    @RequestMapping(value = "/api/v1/templates", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTemplate(@RequestPart("file") MultipartFile file, @Validated FileTemplateDto template) throws Exception {
        var url = storageService.uploadPicture("templates", file);
        var existingTemplate = fileTemplateService.getTemplateByName(template.getName());
        try {
            if (existingTemplate.isEmpty())
                throw new ApplicationException(400, "invalid_request", "Template with specified name doesn't exist.");
            existingTemplate.get().setUrl(url);
            fileTemplateService.save(existingTemplate.get());
            return new ResponseEntity<>(new GenericResponse<>("00", "File updated successfully", url), HttpStatus.OK);
        } finally {
            // Log the template update activity
            activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), FileTemplate.class.getTypeName(), null, null, "Initiated a request to update a template");
        }
    }

    /**
     * Retrieves a file template by its name.
     *
     * This method fetches a file template based on the given name and returns it in the response.
     *
     * @param name the name of the template to retrieve.
     * @return a response entity containing the template details.
     */
    @RequestMapping(value = "/api/v1/templates", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTemplateByName(@RequestParam String name) {
        return new ResponseEntity<>(new GenericResponse<>("00", "Template retrieved successfully", fileTemplateService.getTemplateByName(name)), HttpStatus.OK);
    }
}
