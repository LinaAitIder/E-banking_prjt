import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: `
    <!-- Point d'entrée unique pour le routage -->
    <router-outlet></router-outlet>
  `,
  styles: []
})
export class AppComponent {
  title = 'ebanking-app';
}