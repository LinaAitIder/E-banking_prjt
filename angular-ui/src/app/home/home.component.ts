import { Component } from '@angular/core';


import { BetweenComponent } from './between/between.component';
import { CardsComponent } from './cards/cards.component';

import {NavbarComponent} from './navbar/navbar.component'
import {RouterOutlet} from "@angular/router";

@Component({
    selector: 'app-home',
    standalone: true,
    imports: [NavbarComponent, BetweenComponent, CardsComponent, RouterOutlet],
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss']
})
export class HomeComponent {}
