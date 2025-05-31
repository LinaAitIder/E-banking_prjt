import { Component } from '@angular/core';

import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from './sidebar/sidebar.component';
@Component({
    selector: 'app-espace-agent',
    standalone: true,
    imports: [RouterOutlet, SidebarComponent],
    templateUrl: './Espace-agent.component.html',
    styleUrls: ['./Espace-agent.component.scss']
})
export class EspaceAgentComponent{}

