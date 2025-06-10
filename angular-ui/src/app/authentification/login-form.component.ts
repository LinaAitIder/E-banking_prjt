import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Credential } from '../model/credential.model';
import {ClientService} from "../services/client.service";
import {WebauthnService} from "../services/webauthn.service";
import {AuthenService} from "../services/authen.service";
import {User} from "../model/user.model";
import {NavbarComponent} from "../home/navbar/navbar.component";
import {CommonModule} from "@angular/common";
import {Router} from "@angular/router";

@Component({
    selector: 'app-login-form',
    templateUrl: './login-form.component.html',
    styleUrl:'login-form.component.scss',
    standalone: true,
    imports: [ReactiveFormsModule, NavbarComponent, CommonModule],
})
export class LoginFormComponent {
    loginForm: FormGroup;

    constructor(private fb: FormBuilder,private router: Router, private http: HttpClient,  private authenService: AuthenService, private webAuthnService : WebauthnService ) {
        this.loginForm = this.fb.group({
            email: ['', [Validators.required, Validators.email]],
            password: ['', Validators.required],
        });
    }

     base64urlToArrayBuffer(base64url: string): ArrayBuffer {
        const base64 = base64url.replace(/-/g, '+').replace(/_/g, '/').padEnd(Math.ceil(base64url.length / 4) * 4, '=');
        const binary = window.atob(base64);
        const bytes = new Uint8Array(binary.length);
        for (let i = 0; i < binary.length; i++) {
            bytes[i] = binary.charCodeAt(i);
        }
        return bytes.buffer;
    }


    redirectToSMSVerifPage(){
        this.router.navigate(['/smsVerificationPage'], { queryParams: { purpose: 'login' } });
    }

    onLogin(): void {
        if (this.loginForm.valid) {
            const user:any = this.loginForm.value;
            console.log('Login submitted:', user);

            // Example login request
            this.authenService.verifyPassword(user).subscribe({
                next: (res)=>{
                    console.log("password verified")
                    //Get the challenge and provide biometry service
                    console.log(res);
                    const challenge = this.base64urlToArrayBuffer(res.challenge);
                    const allowedCredentials = res.allowedCredentials.map((credId: string) =>
                        this.base64urlToArrayBuffer(credId)
                    );
                    const userRole = res.role;
                    this.webAuthnService.login(challenge, userRole, user, allowedCredentials);

                },
                error : (error) =>{
                    console.log(error)
                    this.redirectToSMSVerifPage();
                }
            })
        } else {
            this.loginForm.markAllAsTouched();
        }
    }
}
