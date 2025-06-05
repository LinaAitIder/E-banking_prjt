import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TestConnectionService {
  // Replace with your actual backend URL
  private apiUrl = 'http://localhost:8081/E-banking_prjt/api/hello';

  constructor(private http: HttpClient) { }

  testConnectionS() {
    // Send GET request to backend
    return this.http.get(this.apiUrl, {
      responseType: 'text' as 'json', // Expect text response
    });
  }
}
