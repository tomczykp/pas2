import {Component, OnInit, ViewChild} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material/table";
import { AllocationComponent } from '../allocation/allocation.component';
import { ProductDTO } from '../model/productDTO';
import { ReservationDTO } from '../model/reservationDTO';
import { Client } from '../services/client';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import { ActivatedRoute, Router } from '@angular/router'



@Component({
  selector: 'app-reservations',
  templateUrl: './reservations.component.html',
  styleUrls: ['./reservations.component.css']
})
export class ReservationsComponent implements OnInit {

  @ViewChild('paginator') paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  dataSource: MatTableDataSource<ReservationDTO>;
  displayColumns: string[] = ['ID', 'StartDate', 'EndDate', 'ProductID'];

  changePasswordForm = new FormGroup({
    oldPassword: new FormControl('', [Validators.required]),
    newPassword: new FormControl('', [Validators.required]),
    repeatPassword: new FormControl('', [Validators.required])
  })

  newPasswordsNotMatch = false;
  oldPasswordWrong = false;
  passwordChangeSuccesful = false;
  invalidInput = false;

  get oldPassword() {
    return this.changePasswordForm.get('oldPassword');
  }

  get newPassword() {
    return this.changePasswordForm.get('newPassword');
  }

  get repeatPassword() {
    return this.changePasswordForm.get('repeatPassword');
  }

  r: ReservationDTO[];

  constructor(private client: Client, private router: Router) { }

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    this.getMyReservations();
  }

  getMyReservations() {
    this.client.getMyReservations().subscribe((result) => {
      this.r = result;
      this.dataSource = new MatTableDataSource(this.r);
      this.dataSource.paginator = this.paginator;
    })
  }

  onSubmit() {
    if (this.changePasswordForm.valid) {
      let oldPassword = this.changePasswordForm.getRawValue().oldPassword;
      let newPassword = this.changePasswordForm.getRawValue().newPassword;
      let repeatPassword = this.changePasswordForm.getRawValue().repeatPassword;
      if ((newPassword!.toString().length >= 4 && newPassword!.toString.length <= 15)) {
      if (newPassword!.toString() == repeatPassword!.toString()) {
        this.client.passwordChange(newPassword!.toString(), oldPassword!.toString()).subscribe((result) => {
          if (result.status == 200) {
            this.passwordChangeSuccesful = true;
        }
      }, (error) => {
          if (error.status == 400) {
            this.oldPasswordWrong = true;
          }
          this.newPasswordsNotMatch = false;
          this.passwordChangeSuccesful = false;
        }); 
    } else {
      this.newPasswordsNotMatch = true;
      this.passwordChangeSuccesful = false;
      this.oldPasswordWrong = false;
    }
  } else {
    this.invalidInput = true;
    this.newPasswordsNotMatch = false;
    this.oldPasswordWrong = false;
    this.passwordChangeSuccesful = false;
  }
}
}
}
