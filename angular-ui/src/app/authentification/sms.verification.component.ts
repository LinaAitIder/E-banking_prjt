import {Component} from "@angular/core";
import {ReactiveFormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";

@Component({
    selector: 'sms-verification',
    template: '<H1>SMS VERIFICATION</H1>>',
    standalone: true,
    imports: [ReactiveFormsModule, CommonModule]
})

export class SmsVerificationComponent{

}