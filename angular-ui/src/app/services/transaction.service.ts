import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


export interface Transaction {
    type: string;
    amount: number;
    reference: string;
    status: string;
    date: string;
    recipient: string;
}

@Injectable({
    providedIn: 'root'
})
export class TransactionService {
    private apiUrl = 'http://localhost:8080/E-banking_Prjt/api/transactions';

    constructor(private http: HttpClient) { }


    getTransactions(): Observable<Transaction[]> {
        return this.http.get<Transaction[]>(this.apiUrl);
    }



}
