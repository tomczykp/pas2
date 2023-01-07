import { Component, OnInit } from '@angular/core';
import { hasClassName } from '@ng-bootstrap/ng-bootstrap/util/util';
import { Client } from '../services/client';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {

  authenticated = false;
  role : string = "anonymous";

  constructor(public client: Client) { }

  ngOnInit(): void {
    this.client.authenticated.subscribe((change: boolean) => {
      this.authenticated = change;
      if (this.authenticated) {
        this.role = this.client.getRole().toLocaleLowerCase();
      }
    })
  }

  onLogout() {
    this.role = "anonymous";
    this.client.logout();
  }
}
