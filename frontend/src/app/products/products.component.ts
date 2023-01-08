import { JsonPipe } from '@angular/common';
import {Component, OnInit, ViewChild} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material/table";
import { AllocationComponent } from '../allocation/allocation.component';
import { ProductDTO } from '../model/productDTO';
import { Client } from '../services/client';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

  @ViewChild('paginator') paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  dataSource: MatTableDataSource<ProductDTO>;
  displayColumns: string[] = ['ID', "Price", "Allocate"];

  p: ProductDTO[];
  authenticated = false;
  role : string = "anonymous";
  allocationCreationSuccesful = false;
  allocationCreationError = false;

  constructor(private client: Client,
    public dialog: MatDialog) { }

  ngOnInit(): void {
    this.client.authenticated.subscribe((change: boolean) => {
      this.authenticated = change;
      if (this.authenticated) {
        this.role = this.client.getRole().toLocaleLowerCase();
      } else {
        this.role = 'anonymous';
      }
    })
  }

  ngAfterViewInit() {
    this.getProducts();
  }

  getProducts() {
    this.client.products().subscribe((result) => {
      this.p = result;
      this.dataSource = new MatTableDataSource(this.p);
      this.dataSource.paginator = this.paginator;
      if (this.role == 'customer') {
        this.displayColumns = ['ID', "Price", "Allocate"];
      } else {
        this.displayColumns.pop();
      }
    })
  }
  
  openDialog(id: number) {
      const dialogRef = this.dialog.open(AllocationComponent, {
        data: {productID: id, allocationCreationSuc: this.allocationCreationSuccesful, allocationError: this.allocationCreationError}
      });

      dialogRef.afterClosed().subscribe(result => {
        this.allocationCreationError = result.allocationError;
        this.allocationCreationSuccesful = result.allocationCreationSuc;
      });
  }
}
