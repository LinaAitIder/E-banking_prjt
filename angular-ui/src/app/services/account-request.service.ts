import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccountRequest } from '../model/account-request.model';
import { catchError, map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AccountRequestService {
  private apiUrl = 'http://localhost:8080/E-banking_Prjt/api/account-requests';

  constructor(private http: HttpClient) {}

  createAccountRequest(request: any): Observable<any> {
      return this.http.post(this.apiUrl, request, {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'client-id': JSON.parse(localStorage.getItem('userData')!).id.toString()
        })
      });
    }

 getPendingRequests(): Observable<AccountRequest[]> {
     const agentId = JSON.parse(localStorage.getItem('userData')!).id;
     return this.http.get<AccountRequest[]>(`${this.apiUrl}/pending`, {
         headers: new HttpHeaders({
             'agent-id': agentId.toString()
         })
     });
 }

   cancelRequest(requestId: number): Observable<any> {
       return this.http.delete(`${this.apiUrl}/${requestId}`);
     }

  approveRequest(requestId: number): Observable<any> {
    const agentId = JSON.parse(localStorage.getItem('userData')!).id;
    return this.http.post(
      `${this.apiUrl}/${requestId}/approve`,
      {},
      { headers: new HttpHeaders({ 'agent-id': agentId.toString() }) }
    );
  }

  rejectRequest(requestId: number): Observable<any> {
    const agentId = JSON.parse(localStorage.getItem('userData')!).id;
    return this.http.post(
      `${this.apiUrl}/${requestId}/reject`,
      {},
      { headers: new HttpHeaders({ 'agent-id': agentId.toString() }) }
    );
  }
}