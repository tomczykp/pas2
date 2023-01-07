import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import { ActivatedRoute, Router } from '@angular/router'
import { Client } from '../services/client';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required])
  })

  wrongCredentials = false;
  accountInactive = false;
  registerSuccessful = false;
  logoutSuccessful = false;

  constructor(private client: Client,
    private router: Router,
    private route: ActivatedRoute) { 
  }

  get username() {
    return this.loginForm.get('username');
  }

  get password() {
    return this.loginForm.get('password')
  }

  clearPassword() {
    this.loginForm.get('password')?.reset();
  }

  ngOnInit(): void {
    let params = this.route.snapshot.queryParamMap;
    this.wrongCredentials = params.has('')
  }

  onSubmit() {
    if (this.loginForm.valid) {
      let username = this.loginForm.getRawValue().username;
      let password = this.loginForm.getRawValue().password;
      this.client.login(username!.toString(), password!.toString()).subscribe((result) => {
          if (result.status == 200) {
            this.client.saveUserData(result);
            this.client.authenticated.next(true);
            this.router.navigate(['/products']);
          }
        }, (error) => {
          this.client.clearUserData();
          this.client.authenticated.next(false);
          this.clearPassword();
          if (error.status == 400) {
            this.accountInactive = true;
          } else if (error.status == 401) {
            this.wrongCredentials = true;
          }
          this.logoutSuccessful = false;
          this.registerSuccessful = false;
        });
      }
    }
}
