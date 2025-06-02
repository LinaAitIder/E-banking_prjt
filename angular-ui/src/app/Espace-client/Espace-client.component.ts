import { Component } from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';

import {SidebarComponent} from "../Espace-client/sidebar/sidebar.component";

@Component({
    selector: 'app-espace-client',
    standalone: true,
    imports: [
        RouterOutlet,
        SidebarComponent,
    ],
    templateUrl: './Espace-client.component.html',
    styleUrls: ['./Espace-client.component.scss']

})

export class EspaceClientComponent{

}