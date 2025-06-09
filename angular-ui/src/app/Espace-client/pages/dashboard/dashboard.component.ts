import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { HttpClient, HttpClientModule } from "@angular/common/http";

interface Transaction {
    date: string;
    description: string;
    amount: string;
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

    private readonly API_URL = 'http://localhost:8080/E-banking_Prjt/api';

    constructor(private fb: FormBuilder, private http: HttpClient) {
        this.transferForm = this.fb.group({
            recipient: ["", Validators.required],
            amount: ["", [Validators.required, Validators.pattern(/^\d+(\.\d{1,2})?$/)]],
            reason: ["", Validators.required]
        });
        this.loadTransactions();
    }

    private loadTransactions(): void {
        if (!this.userData?.id) return;

        this.isLoading = true;

        this.http.get<Transaction[]>(`${this.API_URL}/transactions/recent`, {
            headers: {
                'client-id': this.userData.id.toString()
            }
        }).subscribe({
            next: (response) => {
                this.transferStatus = "Transfer successful!";
                this.transferForm.reset();
                this.loadTransactions(); // Recharger les transactions après un transfert
            },
            error: (error) => {
                this.transferStatus = "Transfer failed: " +
                    (error.error?.message || error.message || "Unknown error");
            },
            complete: () => {
                this.isLoading = false;
            }
        });
    }

    ngOnInit(): void {
        this.loadUserData();
    }

    private loadUserData(): void {
        try {
            const userDataString = localStorage.getItem("userData");
            this.userData = userDataString ? JSON.parse(userDataString) : null;
        } catch (error) {
            console.error("Error parsing user data:", error);
            this.userData = null;
        }
    }

    getAmountClass(amount: string): string {
        if (amount.startsWith("+")) return "positive";
        if (amount.startsWith("-")) return "negative";
        return "";
    }

    onSubmit(): void {
        if (this.transferForm.invalid || !this.userData?.id) {
            this.transferStatus = "Please fill all fields correctly";
            return;
        }

        this.isLoading = true;
        this.transferStatus = null;

        const transferData = {
            destinationAccount: this.transferForm.value.recipient,
            amount: parseFloat(this.transferForm.value.amount),
            reason: this.transferForm.value.reason
        };

        // Correction de l'URL ici - utilisation de l'URL complète
        this.http.post<any>(`${this.API_URL}/transfer`, transferData, {
            headers: {
                'client-id': this.userData.id.toString(),
                'Content-Type': 'application/json'
            }
        }).subscribe({
            next: (response) => {
                this.isLoading = false;
                this.transferStatus = "Transfer successful: " + response.message;
                this.transferForm.reset();
                this.addTransaction(transferData);
            },
            error: (error) => {
                this.isLoading = false;
                this.transferStatus = this.getErrorMessage(error);
            }
        });
    }

    private addTransaction(transferData: any): void {
        this.transactions.unshift({
            date: new Date().toLocaleDateString(),
            description: `Transfer to ${transferData.destinationUser}`,
            amount: `-$${transferData.amount.toFixed(2)}`
        });
    }

    private getErrorMessage(error: any): string {
        if (error.status === 0) {
            return "Network error - please check your connection";
        }
        return error.error?.message || error.message || "Transfer failed";
    }
}