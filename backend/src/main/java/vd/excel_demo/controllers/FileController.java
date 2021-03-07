package vd.excel_demo.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import vd.excel_demo.models.ResponseMessage;
import vd.excel_demo.services.FileService;
import vd.excel_demo.utils.Constants;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    private static final String DOWNLOADING_FILE = "Downloading Students as excel file";
    private static final String ERROR_DOWNLOADING_FILE = "Error downloading Students as excel file";
    private static final String FILENAME_STUDENTS_XLSX = "attachment; filename=students.xlsx";
    private static final String UPLOAD_FILE = "Uploading the file: ";
    private static final String COULD_NOT_UPLOAD_FILE = "Could not upload the file: ";
    private static final String UPLOAD_EXCEL_FILE = "Please upload an excel file!";
    private static final String UPLOADED_FILE_SUCCESSFULLY = "Uploaded the file successfully: ";

    FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile() {
        LOGGER.info(DOWNLOADING_FILE);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(Constants.APPLICATION_ATOM_XML);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, FILENAME_STUDENTS_XLSX);
            return new ResponseEntity<>(this.fileService.downloadFile(), headers, HttpStatus.OK);
        } catch (IOException e) {
            LOGGER.error(ERROR_DOWNLOADING_FILE + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_DOWNLOADING_FILE);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam MultipartFile file) {
        LOGGER.info(UPLOAD_FILE + file.getOriginalFilename());
        if (this.fileService.isExcelFormat(file)) {
            try {
                this.fileService.uploadFile(file);
                String message = UPLOADED_FILE_SUCCESSFULLY + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                String message = COULD_NOT_UPLOAD_FILE + file.getOriginalFilename();
                LOGGER.error(message + e.getMessage());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(UPLOAD_EXCEL_FILE));
    }
}
