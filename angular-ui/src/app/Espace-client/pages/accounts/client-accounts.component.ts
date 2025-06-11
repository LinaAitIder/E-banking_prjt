import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AccountRequestService } from '../../../services/account-request.service';
import { AccountService } from '../../../services/account.service';
import { CommonModule } from '@angular/common';
import { MessageService } from '../../../services/message.service';

@Component({
    selector: 'app-client-accounts',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './client-accounts.component.html',
    styleUrls: ['./client-accounts.component.scss']
})
export class ClientAccountsComponent implements OnInit {
    accounts: any[] = [];
    pendingAccounts: any[] = []; // Ajout de la propriété manquante
    accountForm!: FormGroup;
    selectedType = "CURRENT";
    successMessage = "";
    errorMessage = "";
    isLoading = false;
    clientId: number;

    constructor(
        private accountService: AccountService,
        private accountRequestService: AccountRequestService,
        private fb: FormBuilder,
        private messageService: MessageService
    ) {
        const userData = localStorage.getItem('userData');
        this.clientId = userData ? JSON.parse(userData).id : null;
    }

    ngOnInit(): void {
        this.initForm();
        this.loadClientAccounts();
        this.loadPendingRequests();
    }

    initForm() {
        this.accountForm = this.fb.group({
            accountType: [this.selectedType, Validators.required],
            currency: ["USD", Validators.required],
            overdraftLimit: [null],
            interestRate: [null],
            supportedCryptos: [""]
        });
    }

    onTypeChange(event: Event): void {
        this.selectedType = (event.target as HTMLSelectElement).value;
    }

    submitAccount() {
        if (this.accountForm.invalid || !this.clientId) return;

        this.isLoading = true;
        const formValue = this.accountForm.value;
        const request = {
            accountType: formValue.accountType,
            currency: formValue.currency,
            overdraftLimit: formValue.accountType === 'CURRENT' ? formValue.overdraftLimit : null,
            interestRate: formValue.accountType === 'SAVINGS' ? formValue.interestRate : null,
            supportedCryptos: formValue.accountType === 'CRYPTO' ?
                   this.parseCryptos(formValue.supportedCryptos) : null
            };

        this.accountRequestService.createAccountRequest(request).subscribe({
            next: () => {
                this.successMessage = "Account request submitted successfully!";
                this.messageService.showSuccess(this.successMessage);
                this.accountForm.reset();
                this.initForm();
                this.loadPendingRequests(); // Recharger les demandes après soumission
                this.isLoading = false;
            },
            error: (err: any) => { // Ajout du type any
                console.error("Request failed:", err);
                this.errorMessage = err.error?.message || "Failed to submit account request, you have to be enrolled with a bank agent";
                this.messageService.showError(this.errorMessage);
                this.isLoading = false;
            }
        });
    }

    private parseCryptos(cryptoString: string): {[key: string]: string} {
        if (!cryptoString) return {};
        return cryptoString.split(',')
            .map(pair => pair.split(':'))
            .reduce((acc, [key, value]) => {
                acc[key.trim()] = value.trim();
                return acc;
            }, {} as {[key: string]: string});
    }

    loadClientAccounts() {
        this.isLoading = true;
        this.accountService.getAccountsByClientAndType(this.clientId, null).subscribe({
            next: (data: any) => {
                this.accounts = data;
                this.isLoading = false;
            },
            error: (err: any) => {
                console.error("Failed to load accounts:", err);
                this.isLoading = false;
            }
        });
    }

    loadPendingRequests() {
        this.isLoading = true;
        this.accountRequestService.getPendingRequests().subscribe({
            next: (data: any) => { // Ajout du type any
                this.pendingAccounts = data;
                this.isLoading = false;
            },
            error: (err: any) => { // Ajout du type any
                console.error("Failed to load pending requests:", err);
                this.isLoading = false;
            }
        });
    }

    cancelRequest(requestId: number) { // Ajout de la méthode manquante
        this.isLoading = true;
        this.accountRequestService.cancelRequest(requestId).subscribe({
            next: () => {
                this.messageService.showSuccess("Request cancelled successfully");
                this.loadPendingRequests();
                this.isLoading = false;
            },
            error: (err: any) => { // Ajout du type any
                console.error("Failed to cancel request:", err);
                this.messageService.showError("Failed to cancel request");
                this.isLoading = false;
            }
        });
    }

    get currentAccounts(): any[] {
        return this.accounts.filter(a => a.type === "CURRENT");
    }

    get savingsAccounts(): any[] {
        return this.accounts.filter(a => a.type === "SAVINGS");
    }

    get cryptoAccounts(): any[] {
        return this.accounts.filter(a => a.type === "CRYPTO");
    }

    getCryptoEntries(cryptos: any): {key: string, value: string}[] {
        if (!cryptos) return [];
        return Object.entries(cryptos).map(([key, value]) => ({key, value: value as string}));
    }
}
