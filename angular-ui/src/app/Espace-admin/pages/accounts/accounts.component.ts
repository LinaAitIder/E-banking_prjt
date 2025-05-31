import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';


interface Account {
    number: string;
    holder: string;
    balance: number;
    type: string;
    created: string;
}

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
        { number: '1234567890', holder: 'John Doe', balance: 5000, type: 'Checking', created: '01/01/2023' },
        { number: '0987653321', holder: 'Jane Smith', balance: 10000, type: 'Savings', created: '02/15/2023' },
        { number: '1122334455', holder: 'Acme Corp', balance: 15000, type: 'Business', created: '03/20/2023' },
        { number: '1029384756', holder: 'Michael Johnson', balance: 2500, type: 'Checking', created: '04/10/2023' },
        { number: '56473829210', holder: 'Mary Williams', balance: 8000, type: 'Savings', created: '05/05/2023' }
    ];
}
