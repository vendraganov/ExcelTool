import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {HttpService} from '../../services/http.service';

@Component({
  selector: 'app-add-student',
  templateUrl: './add-student.component.html',
  styleUrls: ['./add-student.component.scss']
})
export class AddStudentComponent implements OnInit {

  constructor(private router: Router, private httpService: HttpService) { }

  ngOnInit(): void {
  }

  backToAdminPanel(): void {
    this.router.navigate(['admin-panel']);
  }
}
