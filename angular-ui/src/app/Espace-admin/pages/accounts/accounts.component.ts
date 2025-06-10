import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Account } from "../../../model/account.model";
import { AccountService } from "../../../services/account.service";
import { FormsModule } from '@angular/forms'; // Ajouté pour le two-way binding

@Component({
    selector: 'app-accounts',
    standalone: true,
    imports: [CommonModule, FormsModule], // Ajout de FormsModule
    templateUrl: './accounts.component.html',
    styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent implements OnInit {
    summaryCards: any[] = [];
    accounts: Account[] = [];
    filteredAccounts: Account[] = []; // Nouvelle propriété pour les comptes filtrés
    isLoading = true;
    searchTerm: string = ''; // Pour la recherche
    selectedType: string = 'ALL'; // Pour le filtre par type

    constructor(private accountService: AccountService) {}

    ngOnInit(): void {
        this.loadAllAccounts();
    }
    
    private loadAllAccounts(): void {
        this.accountService.getAllAccounts().subscribe({
            next: (accounts) => {
                this.accounts = accounts;
                this.filteredAccounts = [...accounts]; // Initialise filteredAccounts
                this.generateDynamicSummaryCards(accounts);
                this.isLoading = false;
            },
            error: (error) => {
                console.error('Error loading accounts:', error);
                this.isLoading = false;
            }
        });
    }

    // Nouvelle méthode pour filtrer les comptes
    filterAccounts(): void {
        this.filteredAccounts = this.accounts.filter(account => {
            // Filtre par type
            const typeMatch = this.selectedType === 'ALL' || 
                              account.type.toUpperCase() === this.selectedType.toUpperCase();
            
            // Filtre par recherche (holder ou account number)
            const searchMatch = !this.searchTerm || 
                               account.holder.toLowerCase().includes(this.searchTerm.toLowerCase()) || 
                               account.accountNumber.toLowerCase().includes(this.searchTerm.toLowerCase());
            
            return typeMatch && searchMatch;
        });
    }

    // Méthode appelée quand le terme de recherche change
    onSearchChange(): void {
        this.filterAccounts();
    }

    // Méthode appelée quand le type sélectionné change
    onTypeChange(type: string): void {
        this.selectedType = type;
        this.filterAccounts();
    }

    private generateDynamicSummaryCards(accounts: Account[]): void {
        const accountsByType: {[key: string]: {balance: number, count: number, numbers: string[]}} = {};
        
        accounts.forEach(account => {
            if (!accountsByType[account.type]) {
                accountsByType[account.type] = { balance: 0, count: 0, numbers: [] };
            }
            accountsByType[account.type].balance += account.balance;
            accountsByType[account.type].count++;
            accountsByType[account.type].numbers.push(account.accountNumber);
        });

        this.summaryCards = Object.keys(accountsByType).map(type => {
            const data = accountsByType[type];
            return {
                title: this.formatAccountType(type) + (data.count > 1 ? 's' : ''),
                balance: data.balance,
                number: data.numbers.length > 1 ? 
                    `${data.numbers.length} accounts` : 
                    data.numbers[0],
                type: type
            };
        });

        if (this.summaryCards.length === 0) {
            this.summaryCards = [
                { title: 'No Accounts', balance: 0, number: 'N/A', type: 'NONE' }
            ];
        }
    }

    private formatAccountType(type: string): string {
        return type.charAt(0).toUpperCase() + type.slice(1).toLowerCase() + ' Account';
    }
}