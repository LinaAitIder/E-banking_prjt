import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import {Client} from '../../model/client.model'
import {Router, RouterLink, RouterOutlet} from "@angular/router";
import {WebauthnService} from "../../services/webauthn.service";
import {AuthenService} from "../../services/authen.service";
import {firstValueFrom} from "rxjs";
import {NavbarComponent} from "../../home/navbar/navbar.component";

@Component({
    selector: 'app-registration-form',
    templateUrl: './client-registration.component.html',
    styleUrl : 'client-registration.component.scss',
    standalone: true,
    imports: [ReactiveFormsModule, CommonModule, RouterLink, RouterOutlet, NavbarComponent]
})
export class ClientRegistrationComponent {
    registrationForm: FormGroup;
    message: string | null = null;
    isSuccess: boolean = false;
    successMessage = "";
    errorMessage = "";

    constructor(private fb: FormBuilder , private authenService: AuthenService, private webAuthnService : WebauthnService , private router: Router ) {
        this.registrationForm = this.fb.group({
            fullName: ['', Validators.required],
            dateOfBirth: ['', Validators.required],
            nationalId: ['', [Validators.required]],
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(6)]],
            phone: ['', [Validators.required, Validators.pattern(/^\d{8,15}$/)]],
            address: ['', Validators.required],
            city: ['', Validators.required],
            country: ['', Validators.required],
            confirmPassword: ['', Validators.required],
            termsAccepted: [false, Validators.requiredTrue]
        }, { validators: this.passwordMatchValidator });
    }

    //Logic for Password and Password Confirmation Matching
    passwordMatchValidator(form: FormGroup) {
        return form.get('password')?.value === form.get('confirmPassword')?.value
            ? null : { mismatch: true };
    }

    isInvalid(controlName: string): boolean {
        const control = this.registrationForm.get(controlName);
        return !!(control && control.invalid && control.touched);
    }

    //Navigating
    redirectToLogin() {
        this.router.navigate(['/login']);
    }

    redirectToSMSVerifPage(clientData: any) {
        const clientDataString = encodeURIComponent(JSON.stringify(clientData));
        this.router.navigate(['/smsVerificationPage'], {
            queryParams: {
                purpose: 'registration',
                data: clientDataString
            }
        });
    }


    async onSubmitPersonalInfo() {
        if (this.registrationForm.invalid) {
            this.registrationForm.markAllAsTouched();
            console.warn('Form is invalid');
            return;
        }
        // Getting Client Data
        const client: Client = this.registrationForm.value;
        const clientData = {
            ...client,
            dateOfBirth: new Date(client.dateOfBirth).toISOString()
        };

        const role: string = clientData.role;
        // Getting Challenge for biometric service
        try {
            const response = await firstValueFrom(
                //This method will be changed to manage general Users
                this.authenService.getChallengeClient(clientData)
            );
            console.log("Response received : ",response);
            const challengeResponse = response.body.challenge;
            const receivedClient:Client = response.body;
            console.log("THIS IS THE RECEIVED CLIENT :",receivedClient);
            console.log("Challenge received :", challengeResponse);
            if (!challengeResponse) {
                throw new Error("Challenge header missing in response");
            }

            const challengeBuffer = new TextEncoder().encode(challengeResponse);

            console.log(challengeBuffer);
            const registrationSuccess = await this.webAuthnService.registerWebAuthenCredentials(
                challengeBuffer,
                receivedClient,
                role
            );
            console.log(registrationSuccess);
            if (registrationSuccess) {
                this.redirectToLogin();
            } else {
                this.redirectToSMSVerifPage(receivedClient);
            }

            this.errorMessage = "Account Created!";

        } catch (error) {
            console.error('Registration failed:', error);
            this.errorMessage = "Failed to create account. Please try again.";
            window.alert("Something went wrong during registration.");
            this.redirectToSMSVerifPage(clientData);
        }
    }
}
