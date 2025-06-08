import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {Account} from "../../../model/account.model";


@Component({
    selector: 'app-accounts',
    standalone:true,
    imports: [CommonModule],
    templateUrl: './accounts.component.html',
    styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent {
    summaryCards = [
        { title: 'Checking Account', balance: 5000, number: '1234567890' },
        { title: 'Savings Account', balance: 10000, number: '0987653321' },
        { title: 'Business Account', balance: 15000, number: '1122334455' },
    ];
    accounts: Account[] = [
    ];
}
