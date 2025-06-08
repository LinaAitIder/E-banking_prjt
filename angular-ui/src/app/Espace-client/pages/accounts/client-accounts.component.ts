import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Account } from "../../../model/account.model";
import { AccountService } from "../../../services/account.service";

@Component({
    selector: "app-client-accounts",
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: "./client-accounts.component.html",
    styleUrls: ["./client-accounts.component.scss"],
})
export class ClientAccountsComponent implements OnInit {
    accounts: Account[] = [];
    clientId: number | undefined;
    accountForm!: FormGroup;
    selectedType = "CURRENT";
    successMessage = "";
    errorMessage = "";


    constructor(private accountService: AccountService, private fb: FormBuilder) {
        const userData = localStorage.getItem("userData");
        if (userData) {
            try {
                this.clientId = JSON.parse(userData)?.id;
            } catch {}
        }
    }

    ngOnInit(): void {
        this.initForm();
        this.loadClientAccounts();
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
        const value = (event.target as HTMLSelectElement).value;
        this.selectedType = value;
        this.accountForm.patchValue({ accountType: value });
    }


    submitAccount() {
        if (!this.clientId || this.accountForm.invalid) return;

        const formValue = this.accountForm.value;
        const payload: any = {
            accountType: formValue.accountType,
            currency: formValue.currency,
            overdraftLimit:
                (formValue.accountType === "CURRENT" || formValue.accountType === "SAVINGS")
                    ? formValue.overdraftLimit
                    : null,
            interestRate: formValue.accountType === "SAVINGS" ? formValue.interestRate : null,
            supportedCryptos:
                formValue.accountType === "CRYPTO" && formValue.supportedCryptos
                    ? formValue.supportedCryptos.split(",").reduce((acc: any, pair: string) => {
                        const [key, value] = pair.split(":");
                        acc[key.trim()] = value.trim();
                        return acc;
                    }, {})
                    : null
        };

        this.accountService.createAccount(this.clientId, payload).subscribe({
            next: () => {
                this.loadClientAccounts();
                this.successMessage = "Account created successfully!";
                console.log("Account created")
            },
            error: err => {
                console.error("Create account failed:", err)
                this.errorMessage = "Failed to create account. Please try again.";

            }
        });
    }

    loadClientAccounts() {
        if (!this.clientId) return;

        this.accountService.getClientAccountsByType(this.clientId).subscribe({
            next: data => (this.accounts = data),
            error: err => console.error("Failed to load accounts:", err)
        });
    }

    getCryptoEntries(supportedCryptos: { [p: string]: string } | undefined) {
        return Object.entries(supportedCryptos || {}).map(([key, value]) => ({ key, value }));
    }

    get currentAccounts(): Account[] {
        return this.accounts.filter(a => a.accountType === "CURRENT");
    }

    get savingsAccounts(): Account[] {
        return this.accounts.filter(a => a.accountType === "SAVINGS");
    }

    get cryptoAccounts(): Account[] {
        return this.accounts.filter(a => a.accountType === "CRYPTO");
    }

}
