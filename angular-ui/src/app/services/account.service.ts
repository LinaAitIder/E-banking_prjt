import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from "rxjs";
import {AccountRequest} from "../model/account-request.model";
import {Account} from "../model/account.model";
@Injectable({
    providedIn: 'root'
})
export class AccountService {
    constructor(private http: HttpClient) {}
    private apiUrl = 'http://localhost:8081/E-banking_prjt/api';

    //getAllClientAccounts
    verifyClientHasAccounts(clientId: any): Observable<any> {
        const headers = new HttpHeaders({
            'client-id': clientId
        });

        return this.http.get(`${this.apiUrl}/account/has-accounts`, { headers });
    }

    createAccount(clientId: any, accountRequest: AccountRequest): Observable<any> {
        const headers = new HttpHeaders({
            'user-id': clientId
        });

        return this.http.post(`${this.apiUrl}/account/create`, accountRequest, { headers });
    }

    fetchAccountDetails(clientId:any):Observable<any> {
        const headers = new HttpHeaders({
            'account-id': clientId
        });
        return this.http.get(`${this.apiUrl}/account/details`, { headers });
    }

    getClientAccountsByType(clientId: number | undefined, type?: string): Observable<Account[]> {
        let params = new HttpParams();
        if (type) {
            params = params.set('type', type);
        }
        console.log("client id :", clientId);
        return this.http.get<Account[]>(
            `${this.apiUrl}/account/client/${clientId}/accounts`,
            { params }
        );
    }




    deleteAccount(accountId: any): Observable<void> {
        const headers = new HttpHeaders({
            'account-id': accountId.toString()
        });

        return this.http.delete<void>(`${this.apiUrl}/account/delete`, { headers });
    }
}