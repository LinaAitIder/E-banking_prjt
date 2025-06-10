import { Component } from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {SidebarComponent} from "../Espace-client/sidebar/sidebar.component";
import {AssistantComponent} from "../assistant/assistant.component";
import {NgIf} from "@angular/common";
import { AssistantButtonComponent } from '../assistant/assistant-button.component'; 


@Component({
    selector: 'app-espace-client',
    standalone: true,
    imports: [
        RouterOutlet,
        SidebarComponent,
        AssistantComponent,
        NgIf,
        AssistantButtonComponent
    ],
    templateUrl: './Espace-client.component.html',
    styleUrls: ['./Espace-client.component.scss']
})
export class EspaceClientComponent {
  showAssistant = false;

  toggleAssistant() {
    this.showAssistant = !this.showAssistant;
  }

  
}