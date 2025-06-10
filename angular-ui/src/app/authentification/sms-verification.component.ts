import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenService} from "../services/authen.service";
import {CommonModule} from "@angular/common";
import {NavbarComponent} from "../home/navbar/navbar.component";


@Component({
    selector: 'app-sms-verification',
    templateUrl: './sms-verification.component.html',
    imports: [
        ReactiveFormsModule,
        CommonModule,
        NavbarComponent
    ],
    styleUrls: ['./sms-verification.component.scss']
})
export class SmsVerificationComponent implements OnInit {
    smsForm: FormGroup;
    isCodeSent: boolean = false;
    message: string = '';
    error: string = '';
    clientData: any;
    isSending: boolean = false;
    countdown: number = 0;
    countdownInterval: any;
    verificationPurpose : string ='';


    constructor(private fb: FormBuilder, private route: ActivatedRoute, private http: HttpClient, private router: Router, private authenService :AuthenService,   private cdr: ChangeDetectorRef
    ) {
        this.smsForm = this.fb.group({
            phone: ['', [Validators.required]],
            code: ['']
        });
    }

    ngOnInit(): void {
        this.route.queryParams.subscribe(params => {
            this.verificationPurpose = params['purpose'];
            const encodedData = params['data'];

            if (encodedData) {
                try {
                    this.clientData = JSON.parse(decodeURIComponent(encodedData));
                    console.log('Client Data from query:', this.clientData);
                } catch (e) {
                    console.error('Failed to parse clientData from query:', e);
                }
            }
        });
    }


    sendCode() {
        this.isSending = true;
        this.cdr.detectChanges();
        console.log("Sending started", this.isSending);
        const phone = this.smsForm.get('phone')?.value;

        this.authenService.verifyPhoneNumber(phone).subscribe({
            next: () => {
                this.isCodeSent = true;
                this.message = 'Verification code sent.';
                this.error = '';
                this.startCountdown();
            },
            error: err => {
                this.error = 'Failed to send SMS. Try again.';
                this.message = '';
            },
            complete: () => {
                this.isSending = false;
                console.log("Sending complete", this.isSending);
            }
        });
    }



    resendCode() {
        this.sendCode();
    }

    startCountdown() {
        this.countdown = 60;
        if (this.countdownInterval) clearInterval(this.countdownInterval);

        this.countdownInterval = setInterval(() => {
            if (this.countdown > 0) {
                this.countdown--;
            } else {
                clearInterval(this.countdownInterval);
            }
        }, 1000);
    }

    verifyCode() {
        const { phone, code } = this.smsForm.value;
        console.log(this.verificationPurpose);
        if(this.verificationPurpose==='registration'){
            this.authenService.verifySMSCode(phone,code,this.clientData).subscribe({
                next: () => {
                    this.message = 'Phone number verified!';
                    this.error = '';
                    this.router.navigate(['/login']);
                },
                error: err => {
                    this.error = 'Invalid code. Please try again.';
                    this.message = '';
                    console.error(err);
                }
            });
        } else if (this.verificationPurpose === 'login') {
            this.authenService.verifyLogin2FA(phone, code).subscribe({
                next: (res) => {
                    const body = res.body;
                    if (body) {
                        const user = body.user;
                        const role = body.role;
                        const token = body.jwtToken;

                        console.log('User:', user);
                        localStorage.setItem("userData", JSON.stringify(user));
                        localStorage.setItem("token", token);
                        localStorage.setItem("role", role)

                        this.router.navigate(['client']);
                    }

                },
                error: err => {
                    this.error = 'Invalid code. Please try again.';
                    this.message = '';
                    console.error(err);
                }
            });
        }


    }


}
