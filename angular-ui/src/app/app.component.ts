import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {TestConnectionComponent} from "./test-connection/test-connection.component";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, TestConnectionComponent],
  template: `
    <h1>Welcome to {{ title }}!</h1>
    <app-test-connection></app-test-connection>

    <router-outlet/>
  `,
  styles: [],
  standalone: true
})
export class AppComponent {
  title = 'angular-ui';
}
