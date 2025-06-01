import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {Router, RouterOutlet} from "@angular/router";

@Component({
    selector: 'app-navbar',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
    constructor(private router: Router) {}

    redirectToLogin(){
        this.router.navigate(['login']);

    }

    redirectToSignUp(){
        this.router.navigate(['registration']);

    }

}
