import { Component } from '@angular/core';
import {  CommonModule } from '@angular/common';
@Component({
    selector: 'app-recent-activity',
    standalone: true,
    imports: [CommonModule], // <-- Ajouter cette ligne
    templateUrl: './recent-activity.component.html',
    styleUrls: ['./recent-activity.component.scss']
})
export class RecentActivityComponent {
    transactions = [
        { date: '04/22/2024', client: 'John Doe', amount: 2500.00, status: 'Completed' },
        { date: '04/22/2024', client: 'Jane Smith', amount: 1200.00, status: 'Pending' },
        { date: '04/22/2024', client: 'Emily White', amount: 350.00, status: 'Completed' },
        { date: '04/22/2024', client: 'David Wilson', amount: 5000.00, status: 'Failed' }
    ];

    cryptoData = [
        { name: 'BTC', price: 63200 },
        { name: 'ETH', price: 3100 },
        { name: 'SOL', price: 145 }
    ];

    stats = {
        totalBalance: 9542000,
        transactionsCount: 184
    };
}
