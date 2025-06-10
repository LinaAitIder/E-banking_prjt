import { Component, OnInit, ViewEncapsulation } from "@angular/core";
import { CommonModule } from "@angular/common";
import { HttpClientModule } from "@angular/common/http";
import { FormsModule } from "@angular/forms";

import { TransactionService, Transaction } from "../../../services/transaction.service";

@Component({
    selector: 'app-client-transactions',
    standalone: true,
    imports: [CommonModule, HttpClientModule, FormsModule],
    templateUrl: './client-transactions.component.html',
    styleUrls: ['./client-transactions.component.scss'],
    encapsulation: ViewEncapsulation.None
})
export class ClientTransactionsComponent implements OnInit {

    transactions: Transaction[] = [];
    transferAmount: number = 0;
    transferMessage: string = '';

    constructor(private transactionService: TransactionService) {}

    ngOnInit(): void {
        this.fetchTransactions();
    }

    fetchTransactions() {
        this.transactionService.getTransactions().subscribe({
            next: (data) => {
                this.transactions = data;
            },
            error: (err) => {
                console.error('Erreur de chargement des transactions', err);
            }
        });
    }

    transferMoney() {
        if (this.transferAmount <= 0) {
            this.transferMessage = 'Please enter a valid amount.';
            return;
        }

        console.log(`Transfer of ${this.transferAmount} from Current Account to Saving Account.`);

        this.transferMessage = `Saving of ${this.transferAmount} completed successfully.`;

        this.transferAmount = 0;

    }
}
