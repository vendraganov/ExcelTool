import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-not-found',
  templateUrl: './not-found.component.html',
  styleUrls: ['./not-found.component.scss']
})
export class NotFoundComponent implements OnInit {

  defaultMessage = 'The page you are looking for was not found!';

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['login']);
  }
}
