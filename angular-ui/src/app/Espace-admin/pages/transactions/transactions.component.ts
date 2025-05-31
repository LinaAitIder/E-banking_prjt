import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-transactions',
    standalone:true,
    imports:[CommonModule],
    templateUrl: './transactions.component.html',
    styleUrls: ['./transactions.component.scss']
})
export class TransactionsComponent {
    transactions = [
        { date: '04/18/2023', description: 'Amazon', amount: -120.00, status: 'Completed', type: 'Debit' },
        { date: '04/16/2023', description: 'Deposit', amount: 2000.00, status: 'Completed', type: 'Credit' },
        { date: '04/14/2023', description: 'Transfer to Savings', amount: -500.00, status: 'Completed', type: 'Transfer' },
        { date: '04/12/2023', description: 'BTC Purchase', amount: -250.00, status: 'Pending', type: 'Crypto' },
        { date: '04/10/2023', description: 'Gym Membership', amount: -50.00, status: 'Completed', type: 'Debit' },
    ];

    accountsSummary = [
        { name: 'Checking Account', balance: '$2,500.00', number: '1234' },
        { name: 'Savings Account', balance: '$10,000.00', number: '5678' },
        { name: 'Credit Card', balance: '-$50.00', number: '4321' },
        { name: 'Bitcoin Wallet', balance: '0.750 BTC', number: 'bc1qar0' }
    ];
}
