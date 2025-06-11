import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { HttpClient, HttpClientModule } from "@angular/common/http";
import { FormsModule } from "@angular/forms";
import { DatePipe } from '@angular/common';

interface Transaction {
    id: number;
    type: string;
    amount: number;
    reference: string;
    status: string;
    transactionDate: string;
    sourceUser: string;
    destinationUser: string;
    sourceAccount?: string;
    destinationAccount?: string;
}

interface TransferRequest {
    amount: number;
    reference?: string;
}

interface TransferBetweenAccountsRequest {
    amount: number;
    direction: 'TO_SAVINGS' | 'FROM_SAVINGS';
    reference?: string;
}

@Component({
    selector: 'app-client-transactions',
    standalone: true,
    imports: [CommonModule, HttpClientModule, FormsModule],
    templateUrl: './client-transactions.component.html',
    styleUrls: ['./client-transactions.component.scss'],
    providers: [DatePipe]
})
export class ClientTransactionsComponent implements OnInit {
    transactions: Transaction[] = [];
    transferAmount: number = 0;
    transferMessage: string = '';
    isLoading: boolean = false;
    hasSavingsAccount: boolean = false;

    private readonly API_URL = 'http://localhost:8080/E-banking_Prjt/api';

    transferDirection: 'TO_SAVINGS' | 'FROM_SAVINGS' = 'TO_SAVINGS';

    constructor(
        private http: HttpClient,
        private datePipe: DatePipe
    ) {}

    ngOnInit(): void {
        this.fetchTransactions();
        this.checkSavingsAccount();
    }

    checkSavingsAccount() {
        const clientId = JSON.parse(localStorage.getItem('userData') || '{}')?.id;
        if (!clientId) return;

        const headers = {
            'client-id': clientId,
            'Authorization': `Bearer ${localStorage.getItem('authToken') || ''}`
        };

        this.http.get<boolean>(`${this.API_URL}/account/has-savings`, { headers })
            .subscribe({
                next: (hasAccount) => {
                    this.hasSavingsAccount = hasAccount;
                },
                error: (err) => {
                    console.error('Error checking savings account', err);
                }
            });
    }

    fetchTransactions() {
        this.isLoading = true;
        const clientId = JSON.parse(localStorage.getItem('userData') || '{}')?.id;

        if (!clientId) {
            console.error('Client ID not found');
            return;
        }

        const headers = {
            'client-id': clientId,
            'Authorization': `Bearer ${localStorage.getItem('authToken') || ''}`
        };

        this.http.get<Transaction[]>(`${this.API_URL}/transactions/by-client`, { headers })
            .subscribe({
                next: (transactions) => {
                    this.transactions = transactions.map(t => this.mapTransaction(t));
                    this.isLoading = false;
                },
                error: (err) => {
                    console.error('Error loading transactions', err);
                    this.isLoading = false;
                }
            });
    }

    transferMoney() {
        if (this.transferAmount <= 0) {
            this.transferMessage = 'Please enter a valid amount.';
            return;
        }

        if (!this.hasSavingsAccount) {
            this.transferMessage = 'You need a savings account to use this feature.';
            return;
        }

        const clientId = JSON.parse(localStorage.getItem('userData') || '{}')?.id;
        if (!clientId) {
            this.transferMessage = 'Authentication error. Please login again.';
            return;
        }

        const transferRequest: TransferRequest = {
            amount: this.transferAmount,
            reference: 'SAVINGS_TRANSFER'
        };

        const headers = {
            'client-id': clientId,
            'Authorization': `Bearer ${localStorage.getItem('authToken') || ''}`,
            'Content-Type': 'application/json'
        };

        this.isLoading = true;
        this.transferMessage = 'Processing transfer...';

        this.http.post<Transaction>(`${this.API_URL}/transfer/save-money`, transferRequest, { headers })
            .subscribe({
                next: (transaction) => {
                    this.transferMessage = `Successfully saved ${this.transferAmount} MAD to your savings account.`;
                    this.transferAmount = 0;
                    this.fetchTransactions(); // Refresh transactions
                    this.isLoading = false;
                },
                error: (err) => {
                    console.error('Transfer error', err);
                    this.transferMessage = err.error?.message || 'Transfer failed. Please try again.';
                    this.isLoading = false;
                }
            });
    }

    withdrawFromSavings() {
        if (this.transferAmount <= 0) {
            this.transferMessage = 'Please enter a valid amount.';
            return;
        }

        if (!this.hasSavingsAccount) {
            this.transferMessage = 'You need a savings account to use this feature.';
            return;
        }

        const clientId = JSON.parse(localStorage.getItem('userData') || '{}')?.id;
        if (!clientId) {
            this.transferMessage = 'Authentication error. Please login again.';
            return;
        }

        const transferRequest: TransferRequest = {
            amount: this.transferAmount,
            reference: 'WITHDRAW_FROM_SAVINGS'
        };

        const headers = {
            'client-id': clientId,
            'Authorization': `Bearer ${localStorage.getItem('authToken') || ''}`,
            'Content-Type': 'application/json'
        };

        this.isLoading = true;
        this.transferMessage = 'Processing withdrawal...';

        this.http.post<Transaction>(`${this.API_URL}/transfer/withdraw-from-savings`, transferRequest, { headers })
            .subscribe({
                next: (transaction) => {
                    this.transferMessage = `Successfully withdrew ${this.transferAmount} MAD from your savings account.`;
                    this.transferAmount = 0;
                    this.fetchTransactions(); // Refresh transactions
                    this.isLoading = false;
                },
                error: (err) => {
                    console.error('Withdrawal error', err);
                    this.transferMessage = err.error?.message || 'Withdrawal failed. Please try again.';
                    this.isLoading = false;
                }
            });
    }

    private mapTransaction(transaction: any): Transaction {
        const isIncoming = transaction.type.startsWith('INCOMING_');
        const baseType = transaction.type.replace('INCOMING_', '').replace('OUTGOING_', '');

        return {
            ...transaction,
            type: this.getTypeDisplay(baseType, isIncoming),
            amount: isIncoming ? Math.abs(transaction.amount) : -Math.abs(transaction.amount),
            status: this.getStatusDisplay(transaction.status),
            recipient: isIncoming
                ? transaction.sourceUser || transaction.sourceAccount || 'Unknown Sender'
                : transaction.destinationUser || transaction.destinationAccount || 'Unknown Recipient',
            date: this.datePipe.transform(transaction.transactionDate, 'medium') || ''
        };
    }

    private getTypeDisplay(baseType: string, isIncoming: boolean): string {
        const types: {[key: string]: string} = {
            'TRANSFER': isIncoming ? 'Transfer Received' : 'Transfer Sent',
            'DEPOSIT': 'Deposit',
            'WITHDRAWAL': 'Withdrawal',
            'RECHARGE': 'Mobile Recharge',
            'SAVINGS_TRANSFER': 'Savings Transfer',
            'WITHDRAW_FROM_SAVINGS': 'Withdrawal from Savings'
        };
        return types[baseType] || baseType;
    }

    private getStatusDisplay(status: string): string {
        const statusMap: {[key: string]: string} = {
            'COMPLETED': 'Completed',
            'PENDING': 'Pending',
            'FAILED': 'Failed'
        };
        return statusMap[status?.toUpperCase()] || status || 'Pending';
    }

    transferBetweenAccounts() {
        if (this.transferAmount <= 0) {
            this.transferMessage = 'Veuillez entrer un montant valide.';
            return;
        }

        if (!this.hasSavingsAccount) {
            this.transferMessage = 'Vous devez avoir un compte épargne pour utiliser cette fonctionnalité.';
            return;
        }

        const clientId = JSON.parse(localStorage.getItem('userData') || '{}')?.id;
        if (!clientId) {
            this.transferMessage = 'Erreur d\'authentification. Veuillez vous reconnecter.';
            return;
        }

        const endpoint = this.transferDirection === 'TO_SAVINGS'
            ? 'save-money'
            : 'withdraw-from-savings';

        const transferRequest = {
            amount: this.transferAmount,
            reference: this.transferDirection === 'TO_SAVINGS'
                ? 'SAVINGS_TRANSFER'
                : 'WITHDRAW_FROM_SAVINGS'
        };

        const headers = {
            'client-id': clientId,
            'Authorization': `Bearer ${localStorage.getItem('authToken') || ''}`,
            'Content-Type': 'application/json'
        };

        this.isLoading = true;
        this.transferMessage = 'Traitement du transfert en cours...';

        this.http.post<any>(`${this.API_URL}/transfer/${endpoint}`, transferRequest, { headers })
            .subscribe({
                next: (response) => {
                    const action = this.transferDirection === 'TO_SAVINGS'
                        ? 'versé sur votre compte épargne'
                        : 'retiré de votre compte épargne';
                    this.transferMessage = `Montant de ${this.transferAmount} MAD ${action} avec succès.`;
                    this.transferAmount = 0;
                    this.fetchTransactions();
                    this.isLoading = false;
                },
                error: (err) => {
                    console.error('Erreur de transfert', err);
                    this.transferMessage = err.error?.message || 'Échec du transfert. Veuillez réessayer.';
                    this.isLoading = false;
                }
            });
    }
}