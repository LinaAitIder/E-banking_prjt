import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { NavbarComponent } from "../home/navbar/navbar.component";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-registration-type',
  standalone: true,
  imports: [
    RouterOutlet,
    NavbarComponent,
    CommonModule
  ],
  templateUrl: './registration-type.component.html',
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
        this.router.navigate(['/agentRegistration']);
        break;
      case 'admin':
        this.router.navigate(['/admin/login']);
        break;
            default:
                this.router.navigate(['registration']);
        }
    }
}