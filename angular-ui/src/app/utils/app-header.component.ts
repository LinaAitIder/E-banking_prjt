import { Component } from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';

@Component({
    selector: 'app-header',
    imports: [RouterLink],
    template: `
    <a routerLink="/login">login</a>
    <a routerLink="/register">register</a>

    `,
    styles: []
})
export class AppHeader {

}
