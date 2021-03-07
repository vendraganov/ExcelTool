package vd.excel_demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vd.excel_demo.models.Student;
import vd.excel_demo.services.StudentService;

import java.util.List;


@RestController
@RequestMapping("/student")
public class StudentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);

    private static final String STUDENT_WITH_ID_WAS_DELETED = "Student with %d id was deleted";
    private static final String DELETING_STUDENT_WITH_ID_WHICH_DOES_NOT_EXIST = "Trying deleting Student with %d id which does not exist";
    private static final String REQUEST_TO_REGISTER_NEW_STUDENT = "Request to register new Student";
    private static final String ERROR_REGISTER_NEW_STUDENT = "Error! Cannot register new Student! ";

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Student>> getStudents() {
        return new ResponseEntity<>(this.studentService.getStudents(), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Student> saveStudent(@RequestBody Student student){
        LOGGER.info(REQUEST_TO_REGISTER_NEW_STUDENT);
        try {
            return new ResponseEntity<>(this.studentService.saveStudent(student), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            LOGGER.error(ERROR_REGISTER_NEW_STUDENT + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERROR_REGISTER_NEW_STUDENT);
        }
    }

    @DeleteMapping("/delete/{studentId}")
    public ResponseEntity<Boolean> deleteStudent(@PathVariable Long studentId){
        if(this.studentService.deleteStudent(studentId)){
            LOGGER.info(String.format(STUDENT_WITH_ID_WAS_DELETED, studentId));
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        String message = String.format(DELETING_STUDENT_WITH_ID_WHICH_DOES_NOT_EXIST, studentId);
        LOGGER.warn(message);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(message, studentId));
    }
}
