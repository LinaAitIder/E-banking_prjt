import { Component } from '@angular/core';

@Component({
    selector: 'app-top-cards',
    standalone: true,
    templateUrl: './top-cards.component.html',
    styleUrls: ['./top-cards.component.scss']
})
export class TopCardsComponent {
    topCards = [
        {
            icon: 'fas fa-user',
            label: 'Total Clients',
            value: '1,250'
        },
        {
            icon: 'fas fa-wallet',
            label: 'Total Accounts',
            value: '5,200'
        },
        {
            icon: 'fas fa-dollar-sign',
            label: 'Total Balance',
            value: '$9,542,000'
        },
        {
            icon: 'fas fa-sync-alt',
            label: 'Transactions Today',
            value: '184'
        }
    ];
}
