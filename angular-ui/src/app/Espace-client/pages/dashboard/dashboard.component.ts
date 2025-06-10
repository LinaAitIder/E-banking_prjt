import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { HttpClient, HttpClientModule } from "@angular/common/http";
import { DatePipe } from '@angular/common';
import { HttpHeaders } from '@angular/common/http';

interface Transaction {
    id: number;
    transactionDate: string;
    type: string; //with "INCOMING_" or "OUTGOING_" prefix
    amount: number;
    destinationUser: string;
    sourceUser: string;
    destinationAccount?: string;
    sourceAccount?: string;
}

interface AccountResponse {
    id: number;
    accountNumber: string;
    balance: number;
    type: string;
    creationDate: string;
    rib: string ;
}

@Component({
    selector: "app-dashboard",
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, HttpClientModule], // Ajout de HttpClientModule
    templateUrl: "./dashboard.component.html",
    styleUrls: ["./dashboard.component.scss"],
})
export class DashboardComponent implements OnInit {
    transferForm: FormGroup;
    userData: any;
    transferStatus: string | null = null;
    isLoading = false;
    transactions: Transaction[] = [];
    accounts: AccountResponse[] = [];
    mainAccount: AccountResponse | null = null;

    private readonly API_URL = 'http://localhost:8080/E-banking_Prjt/api';

    constructor(private fb: FormBuilder, private http: HttpClient) {
        this.transferForm = this.fb.group({
            recipient: ["", Validators.required],
            amount: ["", [Validators.required, Validators.pattern(/^\d+(\.\d{1,2})?$/)]],
            reason: ["", Validators.required]
        });
    }

    ngOnInit(): void {
        this.initializeComponent();
    }

    private initializeComponent(): void {
        this.loadUserData().then(() => {
            if (this.userData?.id) {
                this.loadAccounts();
                this.loadTransactions();
            } else {
                console.error("User data not available - redirecting to login");
            }
        });
    }

    private loadAccounts(): void {
        this.http.get<AccountResponse[]>(`${this.API_URL}/account/client/${this.userData.id}/accounts`)
            .subscribe({
                next: (accounts) => {
                    console.log('Accounts from API:', accounts);
                    this.accounts = accounts;

                    // 1. Essayer de trouver un compte courant
                    this.mainAccount = accounts.find(account =>
                        account.type === 'COURANT' || account.type === 'CURRENT'
                    ) || null;

                    // 2. Si aucun compte courant, prendre le premier compte
                    if (!this.mainAccount && accounts.length > 0) {
                        this.mainAccount = accounts[0];
                    }

                    // 3. Si aucun compte n'existe, créer un compte par défaut
                    if (!this.mainAccount) {
                        this.mainAccount = this.createDefaultAccount();
                    }

                    // Mise à jour des données utilisateur
                    if (this.mainAccount) {
                        this.updateUserData(this.mainAccount);
                    }
                },
                error: (error) => {
                    console.error("Error loading accounts:", error);
                    this.mainAccount = this.createDefaultAccount();
                }
            });
    }

    private createDefaultAccount(): AccountResponse {
        return {
            id: 0,
            accountNumber: '•••• •••• •••• ••••',
            balance: 0,
            type: 'COURANT',
            creationDate: new Date().toISOString(),
            rib : '•••• •••• •••• ••••'
        };
    }

    private updateUserData(account: AccountResponse): void {
        this.userData = {
            ...this.userData,
            accountNumber: account.accountNumber,
            balance: account.balance,
            rib: account.rib
        };
        localStorage.setItem("userData", JSON.stringify(this.userData));
    }

    private loadUserData(): Promise<void> {
        return new Promise((resolve) => {
            try {
                const userDataString = localStorage.getItem("userData");
                if (userDataString) {
                    this.userData = JSON.parse(userDataString);
                    console.log("User data loaded:", this.userData);
                } else {
                    console.warn("No user data found in localStorage");
                }
            } catch (error) {
                console.error("Error loading user data:", error);
            } finally {
                resolve();
            }
        });
    }

    private loadTransactions(): void {
        if (!this.userData?.id) return;

        this.isLoading = true;
        this.loadCachedTransactions();

        const headers = {
            'client-id': this.userData.id.toString(),
            'Authorization': `Bearer ${localStorage.getItem('authToken') || ''}`
        };

        this.http.get<Transaction[]>(`${this.API_URL}/transactions/recent`, { headers })
            .subscribe({
                next: (transactions) => {
                    this.transactions = transactions.map(t => ({
                        ...t,
                        // Assurez-vous que le montant est négatif pour les sortantes
                        amount: t.type.startsWith('OUTGOING_') ? -Math.abs(t.amount) : Math.abs(t.amount)
                    }));
                    localStorage.setItem(`transactions_${this.userData.id}`, JSON.stringify(this.transactions));
                },
                error: (error) => {
                    console.error("Error loading transactions:", error);
                },
                complete: () => this.isLoading = false
            });
    }

    private loadCachedTransactions(): void {
        const cachedKey = `transactions_${this.userData.id}`;
        const cachedTransactions = localStorage.getItem(cachedKey);
        if (cachedTransactions) {
            try {
                this.transactions = JSON.parse(cachedTransactions);
                // Filtrer les transactions temporaires si nécessaire
                this.transactions = this.transactions.filter(t => t.id !== undefined);
            } catch (e) {
                console.error("Error parsing cached transactions", e);
            }
        }
    }



    getAmountClass(amount: number): string {
        return amount < 0 ? 'negative' : 'positive';
    }

    onSubmit(): void {
        if (this.transferForm.invalid || !this.userData?.id || !this.mainAccount) {
            this.transferStatus = "Please fill all fields correctly";
            return;
        }

        this.isLoading = true;
        const amount = parseFloat(this.transferForm.value.amount);

        /* if (this.mainAccount.balance < amount) {
            this.transferStatus = "Insufficient balance";
            this.isLoading = false;
            return;
        } */

        const transferData = {
            destinationAccount: this.transferForm.value.recipient,
            amount: amount,
            reason: this.transferForm.value.reason,
            destinationUser: this.transferForm.value.recipient
        };

        const headers = new HttpHeaders({
            'client-id': this.userData.id.toString(),
            'Authorization': `Bearer ${localStorage.getItem('authToken') || ''}`
        });

        this.http.post<any>(`${this.API_URL}/transfer`, transferData, { headers })
            .subscribe({
                next: (response) => {
                    this.transferStatus = "Transfer successful";
                    this.transferForm.reset();
                    this.addTransaction(transferData);
                },
                error: (error) => {
                    this.transferStatus = this.getErrorMessage(error);
                },
                complete: () => this.isLoading = false
            });
    }

    private addTransaction(transferData: any): void {
        const newTransaction: Transaction = {
            id: Date.now(),
            transactionDate: new Date().toISOString(),
            type: 'OUTGOING_TRANSFER',
            amount: -transferData.amount,
            destinationUser: transferData.destinationUser || transferData.destinationAccount,
            sourceUser: this.userData.fullName,
            sourceAccount: this.mainAccount?.accountNumber
        };

        this.transactions.unshift(newTransaction);

        // Mise à jour du solde
        if (this.mainAccount) {
            this.mainAccount.balance -= transferData.amount;
            this.updateUserData(this.mainAccount);
        }
    }

    getTransactionDescription(transaction: Transaction): string {
        const isIncoming = transaction.type.startsWith('INCOMING_');
        const baseType = transaction.type.replace('INCOMING_', '').replace('OUTGOING_', '');

        const types: {[key: string]: string} = {
            'TRANSFER': isIncoming
                ? `Transfer from ${transaction.sourceUser || 'sender'}`
                : `Transfer to ${transaction.destinationUser || 'recipient'}`,
            'DEPOSIT': 'Deposit',
            'WITHDRAWAL': 'Withdrawal',
            'RECHARGE': 'Mobile Recharge'
        };

        return types[baseType] || baseType;
    }

    private getErrorMessage(error: any): string {
        if (error.status === 0) {
            return "Network error - please check your connection";
        }
        return error.error?.message || error.message || "Transfer failed";
    }

}