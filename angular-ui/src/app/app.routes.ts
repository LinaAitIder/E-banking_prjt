import { Routes } from '@angular/router';
import {LoginFormComponent} from "./authentification/login-form.component";
import {ClientRegistrationComponent} from "./authentification/client-registration.component";
import {AppComponent} from "./app.component";
import {SmsVerificationComponent} from "./authentification/sms.verification.component";

export const routes: Routes = [
    {
        path : 'login',
        component : LoginFormComponent
    }, {
        path: 'register',
        component: ClientRegistrationComponent
    }, {
        path: 'smsVerificationPage',
        component: SmsVerificationComponent
    }

];
