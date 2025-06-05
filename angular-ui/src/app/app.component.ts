import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import {TestConnectionComponent} from "./test-connection/test-connection.component";
import { AssistantComponent } from './assistant/assistant.component';
import { DashboardComponent } from './dashboard/dashboard.component';


@Component({
  selector: 'app-root',
  imports: [CommonModule, RouterOutlet, TestConnectionComponent,  AssistantComponent, DashboardComponent],
  template: `

    <h1>Welcome to {{ title }}!</h1>
    <app-test-connection></app-test-connection>
    <h1>E-Banking Application</h1>
    <app-assistant></app-assistant>
    <app-dashboard></app-dashboard>

    <router-outlet></router-outlet>


    <router-outlet/>
  `,
  styles: [],
  standalone: true
})
export class AppComponent {
  title = 'angular-ui';
}
