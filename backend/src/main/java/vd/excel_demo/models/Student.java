package vd.excel_demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="students")
public class Student {

    private static final String STUDENT_ID = "student_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String GENDER = "gender";
    private static final String DOB = "date_of_birth";
    private static final String DELETED = "deleted";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = STUDENT_ID)
    private long studentId;

    @NotNull
    @Column(name = FIRST_NAME)
    private String firstName;

    @NotNull
    @Column(name = LAST_NAME)
    private String lastName;

    @NotNull
    @Column(name = GENDER)
    private String gender;

    @NotNull
    @Column(name = DOB)
    private LocalDateTime dob;

    @JsonIgnore
    @Column(name = DELETED)
    private boolean deleted;
}
