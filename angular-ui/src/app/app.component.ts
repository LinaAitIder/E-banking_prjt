import { Component } from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {TestConnectionComponent} from "./test-connection/test-connection.component";
import {ClientRegistrationComponent} from "./authentification/client-registration.component";
import {AppHeader} from "./utils/app-header.component";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, AppHeader],
  template: `
    <app-header></app-header>
    <router-outlet></router-outlet>
  `,
  styles: []
})
export class AppComponent {

}
