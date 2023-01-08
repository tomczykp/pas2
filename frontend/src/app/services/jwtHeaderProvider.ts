import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {catchError, Observable, switchMap, throwError} from 'rxjs';
import {environment} from "../../environments/environment";
import { Client } from './client';
import {Router} from '@angular/router';

@Injectable()
export class jwtHeaderProvider implements HttpInterceptor {

  constructor(
    private client: Client,
    private router: Router,
  ) {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = localStorage.getItem("jwt");
    if (!request.url.startsWith(this.client.url) || !token) {
      return next.handle(request).pipe(catchError((err) => {
        if (err.status === 403) {
          this.client.logout();
        }
        return throwError(() => err)
      }));
    }

    const cloned = request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });

    return next.handle(cloned).pipe(catchError((err) => {
      if (err.status === 403) {
        this.client.logout();
      }
      return throwError(() => err)
    }));
  }
}
