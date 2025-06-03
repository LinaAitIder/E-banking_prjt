import { Component } from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {NavbarComponent} from "../home/navbar/navbar.component";

@Component({
    selector: 'app-registration-type',
    templateUrl: './registration-type.component.html',
    imports: [
        RouterOutlet,
        NavbarComponent
    ],
    styleUrls: ['./registration-type.component.scss']
})
export class RegistrationTypeComponent {
    constructor(private router: Router) {}

    selectUserType(userType: string) {
        switch(userType) {
            case 'client':
                this.router.navigate(['clientRegistration']);
                break;
            case 'agent':
                this.router.navigate(['agentRegistration']);
                break;
            case 'admin':
                this.router.navigate(['adminRegistration']);
                break;
            default:
                this.router.navigate(['registration']);
        }
    }
}