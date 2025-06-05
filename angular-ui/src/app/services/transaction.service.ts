// src/app/services/transaction.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MOCK_TRANSACTIONS } from '../mocks/mock-transactions';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private apiUrl = 'http://localhost:8081/E-banking_prjt/api/transactions'; // À remplacer plus tard
  private mockMode = true; // Passer à false quand le backend sera prêt

  constructor(private http: HttpClient) { }

  getMockTransactions() {
    return MOCK_TRANSACTIONS;
  }

  getTransactions(): Observable<any> {
    if (this.mockMode) {
      return of(this.getMockTransactions());
    } else {
      return this.http.get(this.apiUrl);
    }
  }

  // À utiliser quand le backend sera prêt
  getTransactionsFromAPI(userId: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/user/${userId}`);
  }
}