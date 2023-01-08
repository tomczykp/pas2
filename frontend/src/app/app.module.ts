import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {NgbAlertModule, NgbDateParserFormatter, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NavComponent } from './nav/nav.component';
import { ProductsComponent } from './products/products.component';
import { RegisterComponent } from './register/register.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatTableModule} from "@angular/material/table";
import {MatSortModule} from "@angular/material/sort";
import {MatButtonModule} from "@angular/material/button";
import {MatPaginatorModule} from "@angular/material/paginator";
import { jwtHeaderProvider } from './services/jwtHeaderProvider';
import {MatDialogModule} from '@angular/material/dialog';
import { AllocationComponent } from './allocation/allocation.component';
import { NgbDate, NgbCalendar, NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';
import { ReservationsComponent } from './reservations/reservations.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    NavComponent,
    ProductsComponent,
    RegisterComponent,
    AllocationComponent,
    ReservationsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbAlertModule,
    NgbModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatTableModule,
    MatSortModule,
    MatButtonModule,
    MatPaginatorModule,
    MatDialogModule,
    NgbDatepickerModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: jwtHeaderProvider, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
