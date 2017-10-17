import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Response, Headers, Http} from '@angular/http';

@Injectable()
export class CalculateNetProfitService {

  readonly apiAdress: string = 'http://localhost:8090/calculator/';


  constructor(private http: Http) {}

  calculate(selectedCountry: string, netDailyRate: number): Observable<number> {

    const body = {countryName: selectedCountry, netDailyRate: netDailyRate};

    return this.http.post(this.apiAdress + 'calculate', body).map((res: Response) => res.json());

  }

  getCountries(): Observable<string[]> {

    return this.http.get(this.apiAdress + 'countries').map((res: Response) => res.json());

  }

}
