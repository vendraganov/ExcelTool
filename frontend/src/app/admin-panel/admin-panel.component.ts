import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {HttpService} from '../services/http.service';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.scss']
})
export class AdminPanelComponent implements OnInit {

  private readonly ERROR = 'Error: ';
  private readonly FILE_NAME = 'students';
  private readonly QUALIFIED_NAME = 'download';
  private readonly LINK_TAG_NAME = 'a';

  constructor(private router: Router, private httpService: HttpService) {
  }

  ngOnInit(): void {
  }

  uploadFile(event): void {
    if (event.target.files && event.target.files.length) {
      this.httpService.uploadFile(event.target.files[0])
        .subscribe((responseMessage) => {
          alert(responseMessage.message);
        }, (error: HttpErrorResponse) => {
          alert(this.ERROR + error.error.message);
          if (error.status === 401) {
            this.router.navigate(['login']);
          }
        });
    }
  }

  downloadFile(): void {
    this.httpService.downloadFile().subscribe(
      (response)  => {
        const dataType = response.type;
        const binaryData = [];
        binaryData.push(response);
        const downloadLink = document.createElement(this.LINK_TAG_NAME);
        downloadLink.href = window.URL.createObjectURL(new Blob(binaryData, {type: dataType}));
        downloadLink.setAttribute( this.QUALIFIED_NAME, this.FILE_NAME);
        document.body.appendChild(downloadLink);
        downloadLink.click();
      }, (error: HttpErrorResponse) => {
        alert(this.ERROR + error.error.message);
        if (error.status === 401) {
          this.router.navigate(['login']);
        }
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
    this.router.navigate(['login']);
  }
}
