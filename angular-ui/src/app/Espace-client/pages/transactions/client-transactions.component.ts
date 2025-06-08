import {Component, OnInit, ViewEncapsulation} from "@angular/core";
import {CommonModule} from "@angular/common";

@Component({
    selector: 'app-client-transactions',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './client-transactions.component.html',
    encapsulation: ViewEncapsulation.None
})
export class ClientTransactionsComponent  {
}