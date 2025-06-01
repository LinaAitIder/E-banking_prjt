import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { Admin } from '../../model/admin.model';
import {AuthenService} from "../../services/authen.service";
import {WebauthnService} from "../../services/webauthn.service";
import {RouterOutlet} from "@angular/router";
import {NavbarComponent} from "../../home/navbar/navbar.component";

@Component({
    selector: 'app-admin-registration',
    imports: [
        ReactiveFormsModule,
        RouterOutlet,
        NavbarComponent
    ],
    templateUrl: './admin-registration.component.html',
    styleUrl :'admin-registration.component.scss'
})
export class AdminRegistrationComponent {
    adminForm: FormGroup;

    constructor(private fb: FormBuilder, private authenService: AuthenService, private webAuthnService : WebauthnService) {
        this.adminForm = this.fb.group({
            fullName: ['', Validators.required],
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(6)]],
            phoneNumber: [''],
        });
    }

    // Method Will be updated To handle new 2fa logic
    registerAdmin() {
        if (this.adminForm.valid) {
            const adminData: Admin = this.adminForm.value;
            const role: string = adminData.role;
            console.log('Registering Admin:', adminData);
            this.authenService.getChallenge(adminData, role).subscribe({
                next: (res) =>{
                    console.log('Challenged received:', res);
                    this.webAuthnService.registerWebAuthenCredentials(res.data.challenge,adminData,role).then(r =>{
                        console.log("SuccessfulBiometry registration!!!");
                    });
                }, error: (err) => {
                    console.log('Error saving client', err);
                }
            })
        } else {
            this.adminForm.markAllAsTouched();
            console.warn('Form is invalid');
        }
    }
}
