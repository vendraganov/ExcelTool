import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {HttpService} from '../../services/http.service';
import {Student} from '../../models/student';
import {Subscription} from 'rxjs';
import {HttpErrorResponse} from '@angular/common/http';
import {NgxSpinnerService} from 'ngx-spinner';

@Component({
  selector: 'app-list-students',
  templateUrl: './list-students.component.html',
  styleUrls: ['./list-students.component.scss']
})
export class ListStudentsComponent implements OnInit, OnDestroy {

  readonly COLUMNS: string[] = ['STUDENT ID', 'FIRST NAME', 'LAST NAME', 'GENDER', 'DOB', ''];

  studentsCount = 0;
  pageIndex = 0;
  noStudentsToAdd = false;
  scrollable = true;
  students: Student[] = [];
  studentsSub: Subscription = new Subscription();

  constructor(private router: Router, private httpService: HttpService, private spinner: NgxSpinnerService) {
  }

  ngOnInit(): void {
    this.getStudentsCount();
  }

  ngOnDestroy(): void {
    this.studentsSub.unsubscribe();
  }

  backToAdminPanel(): void {
    this.router.navigate(['admin-panel']);
  }

  onScroll(): void {
    if (this.scrollable && !this.noStudentsToAdd) {
      this.scrollable = false;
      this.getBatchOfStudents();
    }
  }

  getStudentsCount(): void {
    this.spinner.show();
    this.studentsSub = this.httpService.getStudentsCount()
      .subscribe((studentsCount) => {
          this.spinner.hide();
          this.studentsCount = studentsCount;
          if (this.studentsCount > 0) {
            this.getBatchOfStudents();
          }
        },
        (error: HttpErrorResponse) => {
          this.spinner.hide();
          this.httpService.handleError(error);
        });
  }

  getBatchOfStudents(): void {
    this.spinner.show();
    this.studentsSub = this.httpService.getBatchOfStudents(this.pageIndex++)
      .subscribe((students) => {
          this.spinner.hide();
          this.noStudentsToAdd = students.length < 20;
          this.students = this.students.concat(students);
          this.scrollable = true;
        },
        (error: HttpErrorResponse) => {
          this.spinner.hide();
          this.httpService.handleError(error);
        });
  }

  deleteStudent(studentId: string, index: number): void {
    this.spinner.show();
    this.studentsSub = this.httpService.deleteStudent(studentId)
      .subscribe((isDelete: boolean) => {
        this.spinner.hide();
        if (isDelete) {
          --this.studentsCount;
          this.deleteStudentFromList(index);
        }
      }, (error: HttpErrorResponse) => {
        this.spinner.hide();
        this.httpService.handleError(error);
      });
  }

  private deleteStudentFromList(index: number): void {
    this.students.splice(index, 1);
  }
}
