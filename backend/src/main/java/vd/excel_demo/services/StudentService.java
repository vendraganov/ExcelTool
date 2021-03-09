package vd.excel_demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vd.excel_demo.models.Student;
import vd.excel_demo.repositories.StudentRepository;

import java.util.List;

@Service
public class StudentService {

    private final static int BATCH_COUNT = 20;
    private static final String NOT_FOUND = "Student not found with id: ";
    private static final String ATTRIBUTE_NAME = "deleted";

    private final StudentRepository studentRepository;

    @Autowired
    private StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public long getStudentsCount() {
        return this.studentRepository.countAllByDeletedFalse();
    }

    public List<Student> getStudents() {
        return this.studentRepository.findAll();
    }

    public List<Student> getBatchOfStudents(int pageIndex) {
        return this.studentRepository.findAll(this.getStudentByNotDeleteSpec(), PageRequest.of(pageIndex, BATCH_COUNT)).getContent();
    }

    public Student saveStudent(Student student) {
        return this.studentRepository.existsById(student.getStudentId()) ?
                this.updateStudent(student) :
                this.studentRepository.save(student);
    }

    public void saveStudents(List<Student> students) {
        this.studentRepository.saveAll(students);
    }

    public void deleteStudent(Long studentId) {
        Student student = this.studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND + studentId));
       student.setDeleted(true);
       this.saveStudent(student);
    }

    private Student updateStudent(Student studentIn) {
        Student student = this.studentRepository.getOne(studentIn.getStudentId());
        student.setFirstName(studentIn.getFirstName());
        student.setLastName(studentIn.getLastName());
        student.setGender(studentIn.getGender());
        student.setDob(studentIn.getDob());
        return this.studentRepository.save(student);
    }

    private Specification<Student> getStudentByNotDeleteSpec() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(ATTRIBUTE_NAME), false);
    }
}
