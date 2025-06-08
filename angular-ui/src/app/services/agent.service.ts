import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client } from '../model/client.model';

@Injectable({
    providedIn: 'root'
})
export class AgentService {
    private apiUrl = '/api/agent';

    constructor(private http: HttpClient) {}

    getUnenrolledClients(): Observable<Client[]> {
        return this.http.get<Client[]>(`${this.apiUrl}/clients/unenrolled`);
    }

    enrollClient(clientId: number): Observable<any> {
        return this.http.post(`${this.apiUrl}/enroll/${clientId}`, {});
    }

    getAgentClients(): Observable<Client[]> {
        return this.http.get<Client[]>(`${this.apiUrl}/clients`);
    }

    getClientDetails(clientId: number): Observable<Client> {
        return this.http.get<Client>(`${this.apiUrl}/clients/${clientId}/details`);
    }
}