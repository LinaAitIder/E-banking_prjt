import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../environments/environment.prod";


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
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient) { }


    getTransactions(): Observable<Transaction[]> {
        return this.http.get<Transaction[]>(`${this.apiUrl}/transactions`);
    }



}
