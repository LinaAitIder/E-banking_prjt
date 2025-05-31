import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: `
<!-- Point d'entrée unique pour le routage
import {RouterLink, RouterOutlet} from '@angular/router';
import {AppHeader} from "./utils/app-header.component";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, AppHeader],
  template: `
    <app-header></app-header>
    <router-outlet></router-outlet> -->
  `,
  styles: []
})
export class AppComponent {
  title = 'ebanking-app';
}

