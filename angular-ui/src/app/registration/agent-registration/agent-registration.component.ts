import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthenService} from "../../services/authen.service";
import {WebauthnService} from "../../services/webauthn.service";
import {BankAgent} from "../../model/bank-agent.model";
import {Router, RouterOutlet} from "@angular/router";
import {NavbarComponent} from "../../home/navbar/navbar.component";
import {Client} from "../../model/client.model";
import {firstValueFrom} from "rxjs";

@Component({
    selector: 'app-agent-registration',
    imports: [
        ReactiveFormsModule,
        RouterOutlet,
        NavbarComponent
    ],
    templateUrl: './agent-registration.component.html',
    styleUrl : 'agent-registration.component.scss'
})
export class AgentRegistrationComponent {
    agentForm: FormGroup;

    constructor(private fb: FormBuilder, private authenService: AuthenService, private webAuthnService : WebauthnService, private router: Router ) {
        this.agentForm = this.fb.group({
            fullName: ['', Validators.required],
            email: ['', [Validators.required, Validators.email]],
            phoneNumber: ['', Validators.required],
            agencyCode: ['', Validators.required],
            password: ['', [Validators.required, Validators.minLength(6)]]
        });
    }

    redirectToSMSVerifPage(agent: BankAgent){
        this.router.navigate(['/smsVerificationPage']);
    }

    redirectToLogin() {
        this.router.navigate(['/login']);
    }

    async registerAgent() {
        if (this.agentForm.invalid) {
            this.agentForm.markAllAsTouched();
            console.warn('Form is invalid');
            return;
        }
            const agentData:BankAgent = this.agentForm.value;
            const role :string = agentData.role;
            console.log('Registering agent:', agentData);
            try {

                const response = await firstValueFrom(
                    //This method will be changed to manage general Users
                    this.authenService.getChallengeAgent(agentData)
                );
                console.log("Response received : ",response);
                const challengeResponse = response.body.challenge;
                const receivedAgent:BankAgent = response.body;
                console.log("THIS IS THE RECEIVED Bank Agent :",receivedAgent);
                console.log("Challenge received :", challengeResponse);
                if (!challengeResponse) {
                    throw new Error("Challenge header missing in response");
                }
                const challengeBuffer = new TextEncoder().encode(challengeResponse);

                console.log(challengeBuffer);
                const registrationSuccess = await  this.webAuthnService.registerWebAuthenCredentials(
                    challengeBuffer,
                    receivedAgent,
                    role
                );

                console.log(registrationSuccess);
                if (registrationSuccess) {
                    this.redirectToLogin();
                } else {
                    this.redirectToSMSVerifPage(receivedAgent);
                }
            } catch (error) {
                console.error('Registration failed:', error);
                window.alert("Something went wrong during registration.");
                this.redirectToSMSVerifPage(agentData);

            }
        }
}