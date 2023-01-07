import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {environment} from "../../environments/environment";
import { LoginDTO } from '../model/loginDTO';



@Injectable({
    providedIn: 'root'
})
export class Client {

    authenticated = new BehaviorSubject(false);

  constructor(
    private http: HttpClient,
    private router: Router) {
    if (localStorage.getItem("jwt") != null) {
      this.authenticated.next(true);
    }

    window.addEventListener('storage', (event) => {
      if (event.storageArea == localStorage) {
        let token = localStorage.getItem('jwt');
        if(token == undefined) {
          window.location.href = '/login';
        }
      }
    }, false);
  }

  login(username: string, password: string) {
    return this.http.post<LoginDTO>(`http://localhost:8080/rest/api/login`, {username, password}, {observe: 'response'});
  }

  saveUserData(result: any) {
    localStorage.setItem("jwt", result.body.jwt);
    localStorage.setItem("role", result.body.role);
  }

  getRole() : string {
    return localStorage.getItem("role")!;
  }

  logout() {
    this.clearUserData();
    this.authenticated.next(false);
    this.router.navigate(['/products']);
  }

  clearUserData() {
    localStorage.removeItem("jwt");
    localStorage.removeItem("role");
  }
}