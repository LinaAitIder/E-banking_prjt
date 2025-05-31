import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from './sidebar/sidebar.component';

@Component({
    selector: 'app-espace-admin',
    standalone: true,
    imports: [
        RouterOutlet, // Pour <router-outlet>
        SidebarComponent // Pour <app-sidebar>
    ],
    templateUrl: './Espace-admin.component.html',
    styleUrls: ['./Espace-admin.component.scss']

}) export class EspaceAdminComponent {}