import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable()
export class HttpInterceptorService implements HttpInterceptor{

  private readonly TOKEN_KEY = 'token';
  private readonly AUTH_BEARER = 'Bearer ';

  constructor() {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (localStorage.getItem(this.TOKEN_KEY)) {
      const headers: HttpHeaders = new HttpHeaders({Authorization: this.AUTH_BEARER + localStorage.getItem(this.TOKEN_KEY)});
      const modifiedRequest = request.clone(
        {headers}
      );
      return next.handle(modifiedRequest);
    }
    return next.handle(request);
  }
}
