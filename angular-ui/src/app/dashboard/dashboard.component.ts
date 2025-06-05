// src/app/dashboard/dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { TransactionService } from '../services/transaction.service';
import { Chart, registerables } from 'chart.js';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  standalone: true,
  imports: [CommonModule]
})
export class DashboardComponent implements OnInit {
  transactions: any[] = [];
  alerts: string[] = [];
  
  constructor(private transactionService: TransactionService) {
    Chart.register(...registerables);
  }

  ngOnInit(): void {
    this.transactions = this.transactionService.getMockTransactions();
    this.createCharts();
    this.generateAlerts();
    this.analyzeSpending();
  }

  createCharts() {
    this.createPieChart();
    this.createBarChart();
    this.createSavingsTrend();
  }

  private createPieChart() {
    const categories = this.groupByCategory();
    new Chart('pieChart', {
      type: 'pie',
      data: {
        labels: Object.keys(categories),
        datasets: [{
          data: Object.values(categories),
          backgroundColor: [
            '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF'
          ]
        }]
      },
      options: {
        responsive: true,
        plugins: {
          title: {
            display: true,
            text: 'Dépenses par catégorie'
          }
        }
      }
    });
  }

  private createBarChart() {
    const monthlyData = this.groupByMonth();
    new Chart('barChart', {
      type: 'bar',
      data: {
        labels: Object.keys(monthlyData),
        datasets: [{
          label: 'Dépenses mensuelles',
          data: Object.values(monthlyData),
          backgroundColor: '#36A2EB'
        }]
      },
      options: {
        responsive: true,
        plugins: {
          title: {
            display: true,
            text: 'Dépenses par mois'
          }
        }
      }
    });
  }

  private createSavingsTrend() {
    const savingsData = this.calculateSavingsTrend();
    new Chart('savingsChart', {
      type: 'line',
      data: {
        labels: savingsData.dates,
        datasets: [{
          label: 'Évolution des économies',
          data: savingsData.amounts,
          borderColor: '#4BC0C0',
          fill: false
        }]
      },
      options: {
        responsive: true,
        plugins: {
          title: {
            display: true,
            text: 'Tendance des économies'
          }
        }
      }
    });
  }

  private groupByCategory(): {[key: string]: number} {
    const categories: {[key: string]: number} = {};
    this.transactions
      .filter(t => t.amount < 0) // Seulement les dépenses
      .forEach(t => {
        categories[t.category] = (categories[t.category] || 0) + Math.abs(t.amount);
      });
    return categories;
  }

  private groupByMonth(): {[key: string]: number} {
    const months: {[key: string]: number} = {};
    this.transactions
      .filter(t => t.amount < 0) // Seulement les dépenses
      .forEach(t => {
        const month = new Date(t.date).toLocaleString('default', { month: 'long' });
        months[month] = (months[month] || 0) + Math.abs(t.amount);
      });
    return months;
  }

  private calculateSavingsTrend() {
    // Simulation de données d'épargne sur plusieurs mois
    return {
      dates: ['Jan', 'Fév', 'Mar', 'Avr', 'Mai'],
      amounts: [1000, 1500, 1200, 1800, 2000]
    };
  }

  private generateAlerts() {
    const totalSpent = this.calculateTotalSpent();
    const categories = this.groupByCategory();
    const totalIncome = this.calculateTotalIncome();

    if (totalSpent > totalIncome * 0.7) {
      this.alerts.push(`Attention: Vous avez dépensé ${totalSpent}€, soit plus de 70% de vos revenus!`);
    }

    if (categories['Entertainment'] > totalSpent * 0.3) {
      this.alerts.push(`Alerte: Vous dépensez plus de 30% dans les loisirs (${categories['Entertainment']}€)`);
    }
  }

  private analyzeSpending() {
    // Simulation d'analyse IA locale
    const biggestExpense = Math.max(...this.transactions.filter(t => t.amount < 0).map(t => Math.abs(t.amount)));
    const mainCategory = Object.entries(this.groupByCategory()).sort((a, b) => b[1] - a[1])[0];
    
    this.alerts.push(
      `Analyse IA: Votre plus grosse dépense était de ${biggestExpense}€`,
      `Analyse IA: Votre catégorie principale est ${mainCategory[0]} (${mainCategory[1]}€)`
    );
  }

  private calculateTotalSpent(): number {
    return Math.abs(this.transactions
      .filter(t => t.amount < 0)
      .reduce((sum, t) => sum + t.amount, 0));
  }

  private calculateTotalIncome(): number {
    return this.transactions
      .filter(t => t.amount > 0)
      .reduce((sum, t) => sum + t.amount, 0);
  }
}