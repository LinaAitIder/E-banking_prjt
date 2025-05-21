import {Injectable} from "@angular/core";
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs";
import {Client} from "../model/client.model";
import {User} from "../model/user.model";

@Injectable({
    providedIn: 'root'
})

export class AuthenService {
    constructor(private http: HttpClient, private user:User) {}
    getChallenge(user: { email: string; fullName: string; [key: string]: any }, role:string): Observable<any> {
        return this.http.post(`/api/register/challenge/${role}`,user,
            {
                headers: { role: role }
            });
    }

    saveCredentials(credential:any, challenge:any, role:String): Observable<any>{
        return this.http.post(`/webauthn/register/${role}`, credential, challenge);
    }


    verifyPassword(user:User):Observable<any> {
       return this.http.post('/api/verification/password', user);
    }

    loginUser(user:User, credential:any):Observable<any>{
        return this.http.post(`/api/login/${user.role}`, credential);
    }

}