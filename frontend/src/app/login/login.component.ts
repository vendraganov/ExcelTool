import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Login} from '../models/login';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {HttpService} from '../services/http.service';
import {HttpErrorResponse} from '@angular/common/http';
import {NgxSpinnerService} from 'ngx-spinner';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {

  private readonly TOKEN_KEY = 'token';
  private readonly ROLE_KEY = 'role';
  private readonly DEFAULT_VALUE = '';

  errorMessage: string;
  loginForm: FormGroup;
  login: Login;
  loginSub: Subscription = new Subscription();

  constructor(private router: Router, private httpService: HttpService, private spinner: NgxSpinnerService) {
  }

  ngOnInit(): void {
    localStorage.clear();
    this.loginForm = new FormGroup({
      email: new FormControl(this.DEFAULT_VALUE, [Validators.required]),
      password: new FormControl(this.DEFAULT_VALUE, [Validators.required])
    });
  }

  ngOnDestroy(): void {
    this.loginSub.unsubscribe();
  }

  onSubmit(): void {
    this.spinner.show();
    this.login = this.loginForm.value;
    this.loginSub = this.httpService.login(this.login)
      .subscribe((userResponse) => {
          this.spinner.hide();
          localStorage.setItem(this.TOKEN_KEY, userResponse.token);
          localStorage.setItem(this.ROLE_KEY, userResponse.role);
          this.router.navigate(['admin-panel']);
        },
        (error: HttpErrorResponse) => {
          this.spinner.hide();
          this.httpService.handleError(error);
        }
      );
  }
}
