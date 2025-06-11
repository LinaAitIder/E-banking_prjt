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
    mainAccount: AccountResponse | undefined = undefined;

    private readonly API_URL = 'http://localhost:8080/E-banking_Prjt/api';

    constructor(private fb: FormBuilder, private http: HttpClient) {
        this.transferForm = this.fb.group({
            recipient: ["", Validators.required],
            amount: ["", [Validators.required, Validators.pattern(/^\d+(\.\d{1,2})?$/)]],
            reason: ["", Validators.required]
        });
    }



    ngOnInit(): void {
        // Charge d'abord les données en cache
        const savedUser = localStorage.getItem('userData');
        const savedAccount = localStorage.getItem('currentAccount');
        const savedTransactions = localStorage.getItem(`transactions_${this.userData?.id}`);

        if (savedUser) this.userData = JSON.parse(savedUser);
        if (savedAccount) this.mainAccount = JSON.parse(savedAccount);
        if (savedTransactions) this.transactions = JSON.parse(savedTransactions);

        // Puis met à jour avec les données du serveur
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
                    this.accounts = accounts.map(account => ({
                        ...account,
                        rib: account.rib || account.accountNumber
                    }));

                    // Trouver le compte courant
                    this.mainAccount = accounts.find(account =>
                        account.type?.toUpperCase() === 'CURRENT' ||
                        account.accountNumber?.startsWith('ACC-')
                    ) ?? accounts[0] ?? this.createDefaultAccount();

                    console.log('Selected main account:', this.mainAccount);

                    // Mise à jour des données utilisateur
                    if (this.mainAccount) {
                        this.updateUserData(this.mainAccount);
                        localStorage.setItem('currentAccount', JSON.stringify(this.mainAccount));
                    }
                },
                error: (error) => {
                    console.error("Error loading accounts:", error);
                    const savedAccount = localStorage.getItem('currentAccount');
                    this.mainAccount = savedAccount ? JSON.parse(savedAccount) : this.createDefaultAccount();
                }
            });
    }

    private createDefaultAccount(): AccountResponse {
        return {
            id: 0,
            accountNumber: '•••• •••• •••• ••••',
            balance: 0,
            type: 'CURRENT', // Changé de 'COURANT' à 'CURRENT' pour cohérence
            creationDate: new Date().toISOString(),
            rib: '•••• •••• •••• ••••'
        };
    }

    private updateUserData(account: AccountResponse): void {
        console.log('Updating user data with account:', account);
        this.userData = {
            ...this.userData,
            accountNumber: account.accountNumber,
            balance: account.balance,
            rib: account.rib || account.accountNumber
        };
        console.log('Updated user data:', this.userData);
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

        // Charge d'abord les données en cache pour une réponse immédiate
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
                        amount: t.type.startsWith('OUTGOING_') ? -Math.abs(t.amount) : Math.abs(t.amount)
                    }));
                    localStorage.setItem(`transactions_${this.userData.id}`, JSON.stringify(this.transactions));
                },
                error: (error) => {
                    console.error("Error loading transactions:", error);
                    if (this.transactions.length === 0) {
                        this.transferStatus = "Using cached transactions";
                    }
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