import { Component, OnInit, Inject } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Route, Router } from '@angular/router';
import { NgbCalendar, NgbDate, NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';
import { Client } from '../services/client';
import { DialogData } from '../model/DialogData';

@Component({
  selector: 'app-allocation',
  templateUrl: './allocation.component.html',
  styleUrls: ['./allocation.component.css']
})
export class AllocationComponent implements OnInit {

  hoveredDate: NgbDate | null = null;
  fromDate: NgbDate;
  toDate: NgbDate | null = null;

  constructor(
    public dialogRef: MatDialogRef<AllocationComponent>, @Inject(MAT_DIALOG_DATA) public data: DialogData,
     calendar: NgbCalendar, private client: Client, private router: Router) {
      this.fromDate = calendar.getToday();
      this.toDate = calendar.getNext(calendar.getToday(), 'd', 10);
    }

  ngOnInit(): void {
  }

  onDateSelection(date: NgbDate) {
		if (!this.fromDate && !this.toDate) {
			this.fromDate = date;
		} else if (this.fromDate && !this.toDate && date.after(this.fromDate)) {
			this.toDate = date;
		} else {
			this.toDate = null;
			this.fromDate = date;
		}
	}

	isHovered(date: NgbDate) {
		return (
			this.fromDate && !this.toDate && this.hoveredDate && date.after(this.fromDate) && date.before(this.hoveredDate)
		);
	}

	isInside(date: NgbDate) {
		return this.toDate && date.after(this.fromDate) && date.before(this.toDate);
	}

	isRange(date: NgbDate) {
		return (
			date.equals(this.fromDate) ||
			(this.toDate && date.equals(this.toDate)) ||
			this.isInside(date) ||
			this.isHovered(date)
		);
	}


  createAllocation() {
    if (confirm()) {
    if (this.fromDate != null && this.toDate != null) {
      let fromDay = this.fromDate.day.toString();
      let fromMonth = this.fromDate.month.toString();
      let toDay = this.toDate.day.toString();
      let toMonth = this.toDate.month.toString();
      if (fromDay.length == 1) {
        fromDay = '0' + fromDay;
      }
      if (fromMonth.length == 1) {
        fromMonth = '0' + fromMonth;
      }
      if (toDay.length == 1) {
        toDay = '0' + toDay;
      }
      if (toMonth.length == 1) {
        toMonth = '0' + toMonth;
      }
      let from = this.fromDate.year.toString() + "-" + fromMonth + "-" + fromDay;
      let to = this.toDate?.year.toString() + "-" + toMonth + "-" + toDay;
      this.client.createReservation(this.data.productID, from, to)
      .subscribe((result) => {
        if (result.status == 200) {
          this.data.allocationCreationSuc = true;
          this.data.allocationError = false;
          this.dialogRef.close(this.data);
        }
      }, (error) => {
        if (error.status == 409) {
          this.data.allocationCreationSuc = false;
          this.data.allocationError = true;
          this.dialogRef.close(this.data);
        }
      });
    }
  }
}
}
