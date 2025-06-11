import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {environment} from "../environments/environment.prod";

@Injectable({
    providedIn: 'root'
})
export class TestConnectionService {
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient) { }

    testConnectionS() {
        return this.http.get(`${this.apiUrl}/hello`, {
            responseType: 'text' as 'json',
        });
    }
}
