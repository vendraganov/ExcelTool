package vd.excel_demo.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vd.excel_demo.models.ResponseMessage;

import static vd.excel_demo.utils.Constants.FILE_TOO_LARGE;

@ControllerAdvice
public class FileUploadExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadExceptionHandler.class);

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseMessage> handleMaxSizeException(MaxUploadSizeExceededException e) {
        LOGGER.warn(FILE_TOO_LARGE + e.getMessage());
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(FILE_TOO_LARGE));
    }
}
