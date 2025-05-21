import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TestConnectionService {
  private apiUrl = 'http://localhost:8080/E-banking_Prjt/api/hello';

  constructor(private http: HttpClient) { }

  testConnectionS() {
    return this.http.get(this.apiUrl, {
      responseType: 'text' as 'json',
    });
  }
}
