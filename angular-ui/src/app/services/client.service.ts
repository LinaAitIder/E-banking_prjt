import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client } from '../model/client.model';

@Injectable({
    providedIn: 'root'
})
export class ClientService {
    private apiUrl = 'http://localhost:8081/E-banking_prjt/api/clients'; 

    constructor(private http: HttpClient) { }

    getAllClients(): Observable<Client[]> {
        return this.http.get<Client[]>(this.apiUrl);
    }

    searchClients(term: string): Observable<Client[]> {
        return this.http.get<Client[]>(`${this.apiUrl}/search?term=${term}`);
    }
}