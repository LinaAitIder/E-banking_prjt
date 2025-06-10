import { Component, Inject, PLATFORM_ID, OnInit } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { ChartConfiguration, DoughnutController, ArcElement } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { forkJoin } from 'rxjs';

import {
    Chart,
    LineController,
    LineElement,
    PointElement,
    LinearScale,
    Title,
    CategoryScale,
    Legend,
    Tooltip
} from 'chart.js';

import { TransactionService } from '../../../../services/transaction.service';
import { AccountService } from '../../../../services/account.service';
import { TransactionResponse } from '../../../../model/transaction-response.model';
import { Account } from '../../../../model/account.model';

@Component({
    selector: 'app-transactions-overview',
    standalone: true,
    imports: [CommonModule, BaseChartDirective],
    templateUrl: './transactions-overview.component.html',
    styleUrls: ['./transactions-overview.component.scss'],
})
export class TransactionsOverviewComponent implements OnInit {
    isBrowser: boolean;
    isLoading = true;

    // Statistiques globales
    totalTransactions = 0;
    totalAccounts = 0;
    totalBalance = 0;

    constructor(
        @Inject(PLATFORM_ID) private platformId: Object,
        private transactionService: TransactionService,
        private accountService: AccountService
    ) {
        this.isBrowser = isPlatformBrowser(platformId);

        // Enregistre Chart.js uniquement dans le navigateur
        if (this.isBrowser) {
            Chart.register(
                LineController,
                LineElement,
                DoughnutController,
                ArcElement,
                PointElement,
                LinearScale,
                Title,
                CategoryScale,
                Legend,
                Tooltip
            );
        }
    }

    ngOnInit() {
        if (this.isBrowser) {
            this.loadAdminDashboardData();
        }
    }

    // Graphique des transactions par mois
    lineChartData: ChartConfiguration<'line'>['data'] = {
        labels: [],
        datasets: [
            {
                data: [],
                label: 'Nombre de Transactions',
                fill: true,
                tension: 0.4,
                borderColor: '#007bff',
                backgroundColor: 'rgba(0, 123, 255, 0.1)',
            }
        ]
    };

    lineChartOptions: ChartConfiguration<'line'>['options'] = {
        responsive: true,
        plugins: {
            legend: {
                display: true
            },
            title: {
                display: true,
                text: 'Évolution du nombre de transactions'
            },
            tooltip: {
                callbacks: {
                    label: (context) => {
                        return `${context.dataset.label}: ${context.parsed.y} transactions`;
                    }
                }
            }
        },
        scales: {
            y: {
                beginAtZero: true,
                ticks: {
                    stepSize: 1,
                    callback: function(value) {
                        return value + ' trans.';
                    }
                }
            }
        }
    };

    // Graphique de répartition des types de comptes
    accountChartData: { labels: string[]; datasets: { data: number[]; backgroundColor: string[]; borderWidth: number; borderColor: string; hoverOffset: number; }[] } = {
        labels: [],
        datasets: [{
            data: [],
            backgroundColor: [
                '#36a2eb',  // Bleu pour Current
                '#4bc0c0',  // Turquoise pour Savings
                '#ff6384',  // Rose pour Crypto
                '#ffce56',  // Jaune pour autres
                '#9966ff'   // Violet pour autres
            ],
            borderWidth: 2,
            borderColor: '#fff',
            hoverOffset: 8
        }]
    };

    accountChartOptions: ChartConfiguration<'doughnut'>['options'] = {
        responsive: true,
        maintainAspectRatio: false,
        cutout: '60%',
        plugins: {
            legend: {
                position: 'bottom',
                labels: {
                    boxWidth: 15,
                    padding: 15,
                    font: {
                        size: 12
                    }
                }
            },
            title: {
                display: true,
                text: 'Répartition des types de comptes'
            },
            tooltip: {
                callbacks: {
                    label: (context) => {
                        const label = context.label || '';
                        const value = context.parsed;
                        const total = context.dataset.data.reduce((a: number, b: number) => a + b, 0);
                        const percentage = ((value / total) * 100).toFixed(1);
                        return `${label}: ${value} comptes (${percentage}%)`;
                    }
                }
            }
        }
    };

    private loadAdminDashboardData() {
        this.isLoading = true;

        // Charger toutes les données administratives
        forkJoin({
            transactions: this.transactionService.getAllTransactions(),
            accounts: this.accountService.getAllAccounts()
        }).subscribe({
            next: (data) => {
                this.processAdminTransactionsData(data.transactions);
                this.processAdminAccountsData(data.accounts);
                this.calculateGlobalStats(data.transactions, data.accounts);
                this.isLoading = false;
            },
            error: (error) => {
                console.error('Erreur lors du chargement des données administratives:', error);
                this.isLoading = false;
                this.setDefaultAdminData();
            }
        });
    }

    private processAdminTransactionsData(transactions: TransactionResponse[]) {
        // Grouper les transactions par mois pour voir l'évolution
        const transactionsByMonth = this.groupTransactionsByMonth(transactions);
        
        // Obtenir les 6 derniers mois
        const months = this.getLast6Months();
        const monthlyCount = months.map(month => {
            const monthTransactions = transactionsByMonth[month] || [];
            return monthTransactions.length; // Nombre de transactions, pas le montant
        });

        // Mettre à jour les données du graphique
        this.lineChartData = {
            labels: months.map(month => this.formatMonthLabel(month)),
            datasets: [
                {
                    data: monthlyCount,
                    label: 'Nombre de Transactions',
                    fill: true,
                    tension: 0.4,
                    borderColor: '#007bff',
                    backgroundColor: 'rgba(0, 123, 255, 0.1)',
                    pointBackgroundColor: '#007bff',
                    pointBorderColor: '#fff',
                    pointBorderWidth: 2
                }
            ]
        };
    }

    private processAdminAccountsData(accounts: Account[]) {
        // Compter les comptes par type
        const accountsByType: { [key: string]: number } = {};
        
        accounts.forEach(account => {
            let type = account.accountType || account.type || 'Autre';
            
            // Normaliser les noms des types
            type = this.normalizeAccountType(type);
            
            if (!accountsByType[type]) {
                accountsByType[type] = 0;
            }
            accountsByType[type]++;
        });

        // Convertir en format pour le graphique
        const labels = Object.keys(accountsByType);
        const data = Object.values(accountsByType);

        this.accountChartData = {
            labels: labels,
            datasets: [{
                data: data,
                backgroundColor: [
                    '#36a2eb',  // Bleu
                    '#4bc0c0',  // Turquoise
                    '#ff6384',  // Rose
                    '#ffce56',  // Jaune
                    '#9966ff',  // Violet
                    '#ff9f40',  // Orange
                    '#c9cbcf'   // Gris
                ],
                borderWidth: 2,
                borderColor: '#fff',
                hoverOffset: 10
            }]
        };
    }

    private normalizeAccountType(type: string): string {
        const normalizedType = type.toLowerCase();
        
        if (normalizedType.includes('current') || normalizedType.includes('courant')) {
            return 'Compte Courant';
        }
        if (normalizedType.includes('saving') || normalizedType.includes('épargne')) {
            return 'Compte Épargne';
        }
        if (normalizedType.includes('crypto')) {
            return 'Compte Crypto';
        }
       
        
        return type; // Retourner le type original si pas de correspondance
    }

    private calculateGlobalStats(transactions: TransactionResponse[], accounts: Account[]) {
        this.totalTransactions = transactions.length;
        this.totalAccounts = accounts.length;
        this.totalBalance = accounts.reduce((sum, account) => sum + (account.balance || 0), 0);
    }

    private groupTransactionsByMonth(transactions: TransactionResponse[]): { [key: string]: TransactionResponse[] } {
        const grouped: { [key: string]: TransactionResponse[] } = {};
        
        transactions.forEach(transaction => {
            const date = new Date(transaction.transactionDate);
            const monthKey = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}`;
            
            if (!grouped[monthKey]) {
                grouped[monthKey] = [];
            }
            grouped[monthKey].push(transaction);
        });
        
        return grouped;
    }

    private getLast6Months(): string[] {
        const months: string[] = [];
        const now = new Date();
        
        for (let i = 5; i >= 0; i--) {
            const date = new Date(now.getFullYear(), now.getMonth() - i, 1);
            const monthKey = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}`;
            months.push(monthKey);
        }
        
        return months;
    }

    private formatMonthLabel(monthKey: string): string {
        const [year, month] = monthKey.split('-');
        const date = new Date(parseInt(year), parseInt(month) - 1);
        return date.toLocaleDateString('fr-FR', { month: 'short', year: '2-digit' });
    }

    private setDefaultAdminData() {
        // Données par défaut pour l'admin
        this.lineChartData = {
            labels: ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin'],
            datasets: [
                {
                    data: [0, 0, 0, 0, 0, 0],
                    label: 'Aucune donnée disponible',
                    fill: true,
                    tension: 0.4,
                    borderColor: '#cccccc',
                    backgroundColor: 'rgba(204, 204, 204, 0.1)',
                }
            ]
        };

        this.accountChartData = {
            labels: ['Aucune donnée'],
            datasets: [{
                data: [1],
                backgroundColor: ['#cccccc'],
                borderWidth: 0,
                borderColor: '#ffffff',
                hoverOffset: 8
            }]
        };

        this.totalTransactions = 0;
        this.totalAccounts = 0;
        this.totalBalance = 0;
    }

    // Méthode pour rafraîchir les données administratives
    refreshAdminData() {
        if (this.isBrowser) {
            this.loadAdminDashboardData();
        }
    }

    // Méthodes utilitaires pour l'affichage
    formatCurrency(amount: number): string {
        return new Intl.NumberFormat('fr-FR', {
            style: 'currency',
            currency: 'EUR'
        }).format(amount);
    }

    formatNumber(num: number): string {
        return new Intl.NumberFormat('fr-FR').format(num);
    }
}