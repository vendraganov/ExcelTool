import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {HttpService} from '../../services/http.service';

@Component({
  selector: 'app-list-students',
  templateUrl: './list-students.component.html',
  styleUrls: ['./list-students.component.scss']
})
export class ListStudentsComponent implements OnInit {

  constructor(private router: Router, private httpService: HttpService) { }

  ngOnInit(): void {
  }

  backToAdminPanel(): void {
    this.router.navigate(['admin-panel']);
  }
}
