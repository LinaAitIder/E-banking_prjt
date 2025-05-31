import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Credential } from '../model/credential.model';
import {ClientService} from "../services/client.service";
import {WebauthnService} from "../services/webauthn.service";
import {AuthenService} from "../services/authen.service";
import {User} from "../model/user.model";

@Component({
    selector: 'app-login-form',
    templateUrl: './login-form.component.html',
    standalone: true,
    imports: [ReactiveFormsModule],
})
export class LoginFormComponent {
    loginForm: FormGroup;

    constructor(private fb: FormBuilder, private http: HttpClient,  private authenService: AuthenService, private webAuthnService : WebauthnService ) {
        this.loginForm = this.fb.group({
            email: ['', [Validators.required, Validators.email]],
            password: ['', Validators.required],
        });
    }

    onLogin(): void {
        if (this.loginForm.valid) {
            const user: User = this.loginForm.value;
            console.log('Login submitted:', user);

            // Example login request
            this.authenService.verifyPassword(user).subscribe({
                next: (res)=>{
                    console.log("password verified")
                    //Get the challenge and provide biometry service
                    this.webAuthnService.login(res.data.challenge, user, res.data.allowedCredentialss).then(r=> {
                        console.log("Successful 2FA authentication");
                    })
                },
                error : (error) =>{
                    console.log(error)
                }
            })
        } else {
            this.loginForm.markAllAsTouched();
        }
    }
}
