import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import {Client} from '../model/client.model'
import {RouterLink} from "@angular/router";
import {ClientService} from "../services/client.service";
import {WebauthnService} from "../services/webauthn.service";
import {AuthenService} from "../services/authen.service";

@Component({
    selector: 'app-registration-form',
    templateUrl: './client-registration.component.html',
    standalone: true,
    imports: [ReactiveFormsModule, CommonModule, RouterLink]
})
export class ClientRegistrationComponent {
    registrationForm: FormGroup;
    message: string | null = null; // Message to display feedback to the user
    isSuccess: boolean = false;

    constructor(private fb: FormBuilder , private authenService: AuthenService, private webAuthnService : WebauthnService ) {
        this.registrationForm = this.fb.group({
            fullName: ['', Validators.required],
            dateOfBirth: ['', Validators.required],
            nationalId: ['', [Validators.required, Validators.pattern(/^\d{8,15}$/)]],
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(6)]],
            phone: ['', [Validators.required, Validators.pattern(/^\d{8,15}$/)]],
            address: ['', Validators.required],
            city: ['', Validators.required],
            country: ['', Validators.required],
            confirmPassword: ['', Validators.required],
            termsAccepted: [false, Validators.requiredTrue]
        });
    }

    isInvalid(controlName: string): boolean {
        const control = this.registrationForm.get(controlName);
        return !!(control && control.invalid && control.touched);
    }

     onSubmitPersonalInfo(){
        if (this.registrationForm.valid) {
            const clientData: Client = this.registrationForm.value;
            const role :string = clientData.role;
             console.log('Form submitted:', clientData);
            this.authenService.getChallenge(clientData, role).subscribe({
                    next: (res) => {
                        console.log('Challenged received:', res);
                        this.webAuthnService.registerWebAuthenCredentials(res.data.challenge,clientData,role).then(r =>{
                                console.log("SuccessfulBiometry registration!!!");
                        });
                    }, error: (err) => {
                        console.log('Error saving client', err);
                    }
                }
            );
        } else {
            this.registrationForm.markAllAsTouched();
            console.warn('Form is invalid');
        }
    }
}
