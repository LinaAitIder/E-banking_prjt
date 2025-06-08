import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AccountRequestService {
    private apiUrl = '/api/account-requests';

    constructor(private http: HttpClient) {}

    getPendingRequests(): Observable<any[]> {
        return this.http.get<any[]>(`${this.apiUrl}/pending`);
    }

    approveRequest(requestId: number): Observable<any> {
        return this.http.post(`${this.apiUrl}/${requestId}/approve`, {});
    }

    rejectRequest(requestId: number): Observable<any> {
        return this.http.post(`${this.apiUrl}/${requestId}/reject`, {});
    }
}