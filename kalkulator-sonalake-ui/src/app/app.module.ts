import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpModule } from '@angular/http';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { CalculateNetProfitService } from './calculate-net-profit.service';


import { AppComponent } from './app.component';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
  HttpModule,
  NgbModule,
  FormsModule,
  BrowserModule,
  HttpClientModule
  ],
  providers: [CalculateNetProfitService],
  bootstrap: [AppComponent]
})
export class AppModule { }
