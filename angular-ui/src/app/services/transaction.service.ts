import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TransactionResponse } from '../model/transaction-response.model'; 

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private apiUrl = 'http://localhost:8081/E-banking_prjt/api/transactions';

  constructor(private http: HttpClient) { }

  getAllTransactions(): Observable<TransactionResponse[]> {
    return this.http.get<TransactionResponse[]>(`${this.apiUrl}/all`);
  }

  getTransactionsByAccount(accountId: number): Observable<TransactionResponse[]> {
    return this.http.get<TransactionResponse[]>(`${this.apiUrl}/by-account`, {
      headers: { 'account-id': accountId.toString() }
    });
  }
}