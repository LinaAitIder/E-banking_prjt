import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import {SidebarComponent} from "../Espace-client/sidebar/sidebar.component";

@Component({
    selector: 'app-espace-client',
    standalone: true,
    imports: [
        RouterOutlet, // Pour <router-outlet>
        SidebarComponent,
        // Pour <app-sidebar>
    ],
    templateUrl: './Espace-client.component.html',
    styleUrls: ['./Espace-client.component.scss']

}) export class EspaceClientComponent{}