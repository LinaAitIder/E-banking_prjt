import { Component, OnInit } from '@angular/core';
import { ClientService } from '../../../../services/client.service';
import { TransactionService } from '../../../../services/transaction.service';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-top-cards',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './top-cards.component.html',
    styleUrls: ['./top-cards.component.scss']
})
export class TopCardsComponent implements OnInit {
    topCards = [
        {
            icon: 'fas fa-user',
            label: 'Total Clients',
            value: '0',
            loading: true
        },
        {
            icon: 'fas fa-sync-alt',
            label: 'Transactions Today',
            value: '0',
            loading: true
        }
    ];

    constructor(
        private clientService: ClientService,
        private transactionService: TransactionService
    ) {}

    ngOnInit(): void {
        this.loadClientCount();
        this.loadTransactionCount();
    }

    private loadClientCount(): void {
        this.clientService.getAllClients().subscribe({
            next: (clients) => {
                this.topCards[0].value = clients.length.toString();
                this.topCards[0].loading = false;
            },
            error: (error) => {
                console.error('Error loading clients:', error);
                this.topCards[0].value = 'N/A';
                this.topCards[0].loading = false;
            }
        });
    }

    private loadTransactionCount(): void {
        this.transactionService.getAllTransactions().subscribe({
            next: (transactions) => {
                this.topCards[1].value = transactions.length.toString();
                this.topCards[1].loading = false;
            },
            error: (error) => {
                console.error('Error loading transactions:', error);
                this.topCards[1].value = 'N/A';
                this.topCards[1].loading = false;
            }
        });
    }
}
