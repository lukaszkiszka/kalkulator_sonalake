import { Component } from '@angular/core';
import { CalculateNetProfitService } from './calculate-net-profit.service';
import 'rxjs/add/operator/map'


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})



export class AppComponent {
 netDailyRate: number;
 selectedCountry: string;
 countries :Array<string>;
 error : string;
 isError: boolean;
 calculateResponse: number;
 isCalculateResponse: boolean;
 requestedCountry: string;
 

 
  constructor(private calculateNetProfitService:CalculateNetProfitService) {
	this.netDailyRate = 0;
	this.isError = false;
	this.isCalculateResponse = false;
  }
  
	resetClick(){
	
		this.netDailyRate = 0;
		this.isError = false;
		this.isCalculateResponse = false;
		this.selectedCountry = this.countries[0];
		this.requestedCountry = null;
	
	}

  calculateNetProfitClick() {
  
	console.log( "selectedCountry: " + this.selectedCountry);
	console.log( "netDailyRate: " + this.netDailyRate);
	
	this.isCalculateResponse = false;
	
	this.isError = false;
	
	if(this.netDailyRate <= 0){
		this.isError = true;
		this.error = "Net daily rate must grather than 0!";
		return;
	}
	
	
	this.calculateNetProfitService.calculate(this.selectedCountry, this.netDailyRate).subscribe(
            data => {
			
				console.log(data);
				
				this.requestedCountry = this.selectedCountry;
				this.isCalculateResponse = true;
                this.calculateResponse = data
				
            },
			error => {
				
				console.log(error);
			
				this.isError = true;
				
				if(error.status == 0){
					this.error = "Error when call backend API. Please try again later."
				} if(error.status == 400) {
					this.error = "Bad request - check input data."				
				}else {
					this.error = error
				}
			}
		);
  
	
  }
  
  ngOnInit() {
	
    
        this.calculateNetProfitService.getCountries().subscribe(
            data => {
			
				console.log(data);
				
                this.countries = data
				if(this.countries.length > 0) {
					this.selectedCountry = this.countries[0];
				}
            },
			error => {
				
				console.log(error);
			
				this.isError = true;
				
				if(error.status == 0){
					this.error = "Error when call backend API. Please try again later."
				} else {
					this.error = error
				}
			}
		);
  }

 
  
}
