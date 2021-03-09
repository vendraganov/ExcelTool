import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {HttpService} from '../services/http.service';
import {HttpErrorResponse} from '@angular/common/http';
import {NgxSpinnerService} from 'ngx-spinner';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.scss']
})
export class AdminPanelComponent implements OnInit, OnDestroy {

  private readonly FILE_NAME = 'students';
  private readonly QUALIFIED_NAME = 'download';
  private readonly LINK_TAG_NAME = 'a';

  fileSub: Subscription = new Subscription();

  constructor(private router: Router, private httpService: HttpService, private spinner: NgxSpinnerService) {
  }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
    this.fileSub.unsubscribe();
  }

  uploadFile(event): void {
    if (event.target.files && event.target.files.length) {
      this.spinner.show();
      this.fileSub = this.httpService.uploadFile(event.target.files[0])
        .subscribe((responseMessage) => {
          this.spinner.hide();
          alert(responseMessage.message);
        }, (error: HttpErrorResponse) => {
          this.spinner.hide();
          this.httpService.handleError(error);
        });
    }
  }

  downloadFile(): void {
    this.spinner.show();
    this.fileSub = this.httpService.downloadFile()
      .subscribe((response) => {
        this.spinner.hide();
        const dataType = response.type;
        const binaryData = [];
        binaryData.push(response);
        const downloadLink = document.createElement(this.LINK_TAG_NAME);
        downloadLink.href = window.URL.createObjectURL(new Blob(binaryData, {type: dataType}));
        downloadLink.setAttribute(this.QUALIFIED_NAME, this.FILE_NAME);
        document.body.appendChild(downloadLink);
        downloadLink.click();
      }, (error: HttpErrorResponse) => {
        this.spinner.hide();
        this.httpService.handleError(error);
      });
  }

  addStudent(): void {
    this.router.navigate(['admin-panel/add-student']);
  }

  listStudents(): void {
    this.router.navigate(['admin-panel/list-students']);
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/']);
  }
}
