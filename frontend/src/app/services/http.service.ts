import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Login} from '../models/login';
import {Observable} from 'rxjs';
import {UserResponse} from '../models/user-response';
import {ResponseMessage} from '../models/response-message';
import {Student} from '../models/student';
import {Router} from '@angular/router';

@Injectable({providedIn: 'root'})
export class HttpService {

  private readonly SERVICE_URL = 'http://localhost:8080';

  private readonly LOGIN_URL = this.SERVICE_URL + '/user/login';

  private readonly STUDENT_COUNT_URL = this.SERVICE_URL + '/student/count';
  private readonly STUDENT_BATCH_URL = this.SERVICE_URL + '/student/batch';
  private readonly STUDENT_SAVE_URL = this.SERVICE_URL + '/student/save';
  private readonly STUDENT_DELETE_URL = this.SERVICE_URL + '/student/delete';

  private readonly UPLOAD_FILE_URL = this.SERVICE_URL + '/file/upload/';
  private readonly DOWNLOAD_FILE_URL = this.SERVICE_URL + '/file/download/';

  private readonly RESPONSE_TYPE = 'blob';
  private readonly FILE = 'file';

  constructor(private httpClient: HttpClient, private router: Router) {
  }

  login(login: Login): Observable<UserResponse> {
    return this.httpClient.post<UserResponse>(this.LOGIN_URL, login);
  }

  saveStudent(student: Student): Observable<Student> {
    return this.httpClient.post<Student>(this.STUDENT_SAVE_URL, student);
  }

  getStudentsCount(): Observable<number> {
    return this.httpClient.get<number>(this.STUDENT_COUNT_URL);
  }

  getBatchOfStudents(startIndex: number): Observable<Student[]> {
    const url = `${this.STUDENT_BATCH_URL}/${startIndex}`;
    return this.httpClient.get<Student[]>(url);
  }

  deleteStudent(studentId: string): Observable<boolean> {
    const url = `${this.STUDENT_DELETE_URL}/${studentId}`;
    return this.httpClient.delete<boolean>(url);
  }

  uploadFile(file: File): Observable<ResponseMessage> {
    const formData: any = new FormData();
    formData.append(this.FILE, file);
    return this.httpClient.post<ResponseMessage>(this.UPLOAD_FILE_URL, formData);
  }

  downloadFile(): Observable<any> {
    return this.httpClient.get( this.DOWNLOAD_FILE_URL, { responseType: this.RESPONSE_TYPE});
  }

  handleError(error: HttpErrorResponse): void {
    if (error.error.error) {
      alert(error.error.message);
    } else {
      alert(error.error);
    }
    if (error.status === 401) {
      this.router.navigate(['/']);
    }
  }
}
