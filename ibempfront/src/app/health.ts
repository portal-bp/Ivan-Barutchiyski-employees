import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HealthService {

  base: string = 'http://localhost:8080'

  constructor(private http: HttpClient) {
  }

  public getHealth(): Observable<string> {
    return this.http.get<string>(this.base + '/health')
  }

}
