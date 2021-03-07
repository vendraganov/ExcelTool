package vd.excel_demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vd.excel_demo.models.Student;
import vd.excel_demo.repositories.StudentRepository;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    private StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return this.studentRepository.findAll();
    }

    public Student saveStudent(Student student) {
        return this.studentRepository.existsById(student.getStudentId()) ?
                this.updateStudent(student) :
                this.studentRepository.save(student);
    }

    public void saveStudents(List<Student> students) {
        this.studentRepository.saveAll(students);
    }

    public boolean deleteStudent(Long studentId) {
        if(this.studentRepository.existsById(studentId)) {
            this.studentRepository.deleteById(studentId);
            return true;
        }
        return false;
    }

    private Student updateStudent(Student studentIn) {
        Student student = this.studentRepository.getOne(studentIn.getStudentId());
        student.setFirstName(studentIn.getFirstName());
        student.setLastName(studentIn.getLastName());
        student.setGender(studentIn.getGender());
        student.setDob(studentIn.getDob());
        return this.studentRepository.save(student);
    }
}
