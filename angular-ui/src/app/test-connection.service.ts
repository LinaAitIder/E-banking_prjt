import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TestConnectionService {
<<<<<<< HEAD
=======
  // Replace with your actual backend URL
>>>>>>> 7e5c1cedb241ff8d14ef44b5bcff4d09a42ddbbf
  private apiUrl = 'http://localhost:8080/E-banking_Prjt/api/hello';

  constructor(private http: HttpClient) { }

  testConnectionS() {
<<<<<<< HEAD
    return this.http.get(this.apiUrl, {
      responseType: 'text' as 'json',
=======
    // Send GET request to backend
    return this.http.get(this.apiUrl, {
      responseType: 'text' as 'json', // Expect text response
>>>>>>> 7e5c1cedb241ff8d14ef44b5bcff4d09a42ddbbf
    });
  }
}
