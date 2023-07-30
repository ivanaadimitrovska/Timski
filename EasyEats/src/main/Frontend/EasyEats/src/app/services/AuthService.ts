import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import {UserService} from "./UserService";


const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  })
};

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient, private service: UserService) { }

  login(username: string, password: string): Observable<any> {

    return this.http.post(
      'http://localhost:8081/api/login', {username, password}, httpOptions);
  }


  logout(): Observable<any> {
    this.service.clean()
    return this.http.get('http://localhost:8081/api/logout', httpOptions);
  }

}