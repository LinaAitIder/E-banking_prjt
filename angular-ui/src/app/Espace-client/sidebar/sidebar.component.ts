import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
    selector: 'app-sidebar',
    standalone: true,
    imports: [RouterLink, RouterLinkActive],
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {
    constructor(private router: Router) {}

    logout(): void {
        console.log("removing Items from localStorage ...")
        localStorage.removeItem('token');
        localStorage.removeItem('userData');
        localStorage.removeItem('role');
        const userData = JSON.parse(localStorage.getItem('userData')!);
        console.log(userData);
        this.router.navigate(['/login']);
    }
}
