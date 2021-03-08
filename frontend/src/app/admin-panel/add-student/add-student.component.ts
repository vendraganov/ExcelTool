import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {HttpService} from '../../services/http.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Student} from '../../models/student';
import {HttpErrorResponse} from '@angular/common/http';
import {Subscription} from 'rxjs';
import {NgxSpinnerService} from 'ngx-spinner';

@Component({
    selector: 'app-add-student',
    templateUrl: './add-student.component.html',
    styleUrls: ['./add-student.component.scss']
})
export class AddStudentComponent implements OnInit, OnDestroy {

    private readonly DEFAULT_VALUE = '';
    private readonly DOB_KEY = 'dob';
    private readonly STUDENT_SAVED_SUCCESSFULLY = 'Student saved successfully!';

    studentForm: FormGroup;
    student: Student;
    studentSub: Subscription = new Subscription();

    constructor(private router: Router, private httpService: HttpService, private spinner: NgxSpinnerService) {
    }

    ngOnInit(): void {
        this.studentForm = new FormGroup({
            firstName: new FormControl(this.DEFAULT_VALUE, [Validators.required]),
            lastName: new FormControl(this.DEFAULT_VALUE, [Validators.required]),
            gender: new FormControl(this.DEFAULT_VALUE, [Validators.required]),
            dob: new FormControl(this.DEFAULT_VALUE, [Validators.required])
        });
    }

    ngOnDestroy(): void {
        this.studentSub.unsubscribe();
    }

    backToAdminPanel(): void {
        this.router.navigate(['admin-panel']);
    }

    onSubmit(): void {
        this.spinner.show();
        this.studentForm.patchValue({dob: new Date(this.studentForm.get(this.DOB_KEY).value)});
        this.student = this.studentForm.value;
        this.studentSub = this.httpService.saveStudent(this.student)
            .subscribe((student) => {
                    this.spinner.hide();
                    if (student) {
                        alert(this.STUDENT_SAVED_SUCCESSFULLY);
                        this.router.navigate(['admin-panel']);
                    }
                },
                (error: HttpErrorResponse) => {
                    this.spinner.hide();
                    this.studentForm.reset();
                    if (error.error.error) {
                        alert(error.error.message);
                    } else {
                        alert(error.error);
                    }
                    if (error.status === 401) {
                        this.router.navigate(['login']);
                    }
                }
            );
    }
}
