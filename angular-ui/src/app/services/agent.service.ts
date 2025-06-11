import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { Client, Account } from '../model/client.model';
import { catchError, map } from 'rxjs/operators';
import { HttpHeaders } from '@angular/common/http';



@Injectable({
    providedIn: 'root'
})
export class AgentService {
    private apiUrl = 'http://localhost:8080/E-banking_Prjt/api/agent';

    constructor(private http: HttpClient) {}

    getUnenrolledClients(): Observable<Client[]> {
        return this.http.get<Client[]>(`${this.apiUrl}/clients/unenrolled`, {
          headers: new HttpHeaders({
            'Content-Type': 'application/json',
            'Accept': 'application/json'
          })
        }).pipe(
          catchError(error => {
            console.error('API Error:', error);
            return throwError(() => new Error('Failed to load clients'));
          })
        );
      }


    enrollClient(clientId: number): Observable<any> {
      const agentId = JSON.parse(localStorage.getItem('userData')!).id;

      return this.http.post(
        `${this.apiUrl}/enroll/${clientId}`,
        {},
        {
          headers: new HttpHeaders({
            'agent-id': agentId.toString(), // Envoi de l'ID de l'agent dans les headers
            'Content-Type': 'application/json'
          })
        }
      );
    }

     getAgentClients(): Observable<Client[]> {
         const agentData = localStorage.getItem('userData');
         if (!agentData) {
             return throwError(() => new Error('Agent data not found'));
         }

         const agentId = JSON.parse(agentData).id;

         return this.http.get<any[]>(`${this.apiUrl}/clients`, {
             headers: new HttpHeaders({
                 'agent-id': agentId.toString(),
                 'Accept': 'application/json'
             })
         }).pipe(
             map((response: any[]) => {
                 return response.map(item => ({
                     id: item.id,
                     fullName: item.fullName,
                     email: item.email,
                     phone: item.phone,
                     isEnrolled: true,
                     responsibleAgent: item.responsibleAgent ? {
                         id: item.responsibleAgent.id,
                         fullName: item.responsibleAgent.fullName,
                         agentCode: item.responsibleAgent.agentCode
                     } : null,
                     accounts: item.accounts?.map((acc: any) => ({
                         id: acc.id,
                         accountNumber: acc.accountNumber,
                         type: acc.type,
                         balance: acc.balance,
                         currency: acc.currency
                     })) || []
                 } as Client));
             }),
             catchError(error => {
                 console.error('API Error:', error);
                 return throwError(() => new Error('Failed to load agent clients'));
             })
         );
     }


    getClientDetails(clientId: number): Observable<Client> {
        return this.http.get<Client>(`${this.apiUrl}/clients/${clientId}`).pipe(
            map((response: any) => ({
                ...response,
                isEnrolled: true,
                accounts: response.accounts?.map((account: any) => ({
                    ...account,
                    createdAt: account.createdAt ? new Date(account.createdAt) : null,
                    // Conversion des champs specifiques
                    overdraftLimit: account.overdraftLimit || undefined,
                    interestRate: account.interestRate || undefined,
                    walletAddress: account.walletAddress || undefined
                })) || [],
                responsibleAgent: response.responsibleAgent ? {
                    id: response.responsibleAgent.id,
                    fullName: response.responsibleAgent.fullName,
                    agentCode: response.responsibleAgent.agentCode,
                    agency: response.responsibleAgent.agency
                } : null
            })),
            catchError(error => {
                console.error('Error loading client details:', error);
                return throwError(() => new Error('Failed to load client details'));
            })
        );
    }

}