import {Injectable} from '@angular/core';
import {BehaviorSubject, subscribeOn} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {environment} from "../../environments/environment";
import { LoginDTO } from '../model/loginDTO';
import { ProductDTO } from '../model/productDTO';
import { Observable } from 'rxjs/internal/Observable';
import { ReservationDTO } from '../model/reservationDTO';



@Injectable({
    providedIn: 'root'
})
export class Client {

    authenticated = new BehaviorSubject(false);
    url: string = 'https://localhost:8181/rest/api'; 

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
    return this.http.post<LoginDTO>(this.url + '/login', {username, password}, {observe: 'response'});
  }

  products() {
    return this.http.get<ProductDTO[]>(this.url + '/product');
  }
  
  createReservation(product: number, startDate: string, endDate: string) {
    return this.http.put(this.url + '/reservation', {startDate, endDate, product}, {observe: 'response'});
  }

  saveUserData(result: any) {
    localStorage.setItem("jwt", result.body.jwt);
    localStorage.setItem("role", result.body.role);
  }

  getMyReservations() {
    return this.http.get<ReservationDTO[]>(this.url + '/customer/reservations');
  }

  passwordChange(newPassword: string, oldPassword: string) {
    return this.http.put(this.url + '/passwordChange', {newPassword, oldPassword}, {observe: 'response'});
  }

  getRole() : string {
    return localStorage.getItem("role")!;
  }

  logout() {
    this.clearUserData();
    this.authenticated.next(false);
    this.router.navigate(['/login'], {queryParams: {'logout-success': true}});
    }

  clearUserData() {
    localStorage.removeItem("jwt");
    localStorage.removeItem("role");
  }
}