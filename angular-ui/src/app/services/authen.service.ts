import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from "rxjs";
import {Client} from "../model/client.model";
import {User} from "../model/user.model";

@Injectable({
    providedIn: 'root'
})


export class AuthenService {
    constructor(private http: HttpClient) {}
    private apiUrl = 'http://localhost:8080/E-banking_Prjt/api';

    getChallenge(user: { email: string; fullName: string; [key: string]: any }, role:string): Observable<any> {
        return this.http.post(`api/auth/register/${role}`,user,
            {
                headers: { role: role }
            });
    }

    // Testing for only the actor : client
    getChallengeClient(user: { email: string; fullName: string; [key: string]: any }): Observable<any> {
        return this.http.post(`${this.apiUrl}/auth/register`,user,{
                observe: 'response'
            });
    }


    verifyAuthenticatorRequest(attestation: AttestationConveyancePreference, client: {
        email: string;
        fullName: string;
        [p: string]: any
    }){
        const params = new HttpParams().set('attestation', attestation);
        return this.http.post(`${this.apiUrl}/auth/register/verify`,client,  { params });
    }

    saveCredentials(credential:any, challenge:any, role:String): Observable<any>{
        return this.http.post(`/webauthn/register/${role}`, credential, challenge);
    }


    verifyPassword(user:User):Observable<any> {
       return this.http.post(`${this.apiUrl}/auth/verification/password`, user);
    }

    loginUser(user:User, credential:any):Observable<any>{
        return this.http.post(`/api/login/${user.role}`, credential);
    }

}