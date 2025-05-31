import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthenService} from "../services/authen.service";
import {WebauthnService} from "../services/webauthn.service";
import {BankAgent} from "../model/bank-agent.model";

@Component({
    selector: 'app-agent-registration',
    imports: [
        ReactiveFormsModule
    ],
    templateUrl: './agent-registration.component.html'
})
export class AgentRegistrationComponent {
    agentForm: FormGroup;

    constructor(private fb: FormBuilder, private authenService: AuthenService, private webAuthnService : WebauthnService) {
        this.agentForm = this.fb.group({
            fullName: ['', Validators.required],
            email: ['', [Validators.required, Validators.email]],
            phoneNumber: ['', Validators.required],
            agencyCode: ['', Validators.required],
            password: ['', [Validators.required, Validators.minLength(6)]]
        });
    }

    registerAgent() {
        if (this.agentForm.valid) {
            const agentData:BankAgent = this.agentForm.value;
            const role :string = agentData.role;
            console.log('Registering agent:', agentData);
            this.authenService.getChallenge(agentData, role).subscribe({
                next: (res) =>{
                    console.log('Challenged received:', res);
                    this.webAuthnService.registerWebAuthenCredentials(res.data.challenge,agentData,role).then(r =>{
                        console.log("SuccessfulBiometry registration!!!");
                    });
                }, error: (err) => {
                    console.log('Error saving client', err);
                }
            })
        } else {
            this.agentForm.markAllAsTouched();
            console.warn('Form is invalid');
        }
    }
}