import { Component } from '@angular/core';
import {Router} from "@angular/router";

@Component({
    selector: 'app-between',
    standalone: true,
    templateUrl: './between.component.html',
    styleUrls: ['./between.component.scss']
})
export class BetweenComponent {

    constructor(private router: Router) {}

    redirectToRegistration() {
        this.router.navigate(['registration']).then(r => console.log(r));
    }
}
