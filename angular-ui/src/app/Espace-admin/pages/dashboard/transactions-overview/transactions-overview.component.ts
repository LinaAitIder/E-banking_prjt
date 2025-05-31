import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { ChartConfiguration ,DoughnutController, ArcElement} from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';


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

@Component({
    selector: 'app-transactions-overview',
    standalone: true,
    imports: [CommonModule, BaseChartDirective],
    templateUrl: './transactions-overview.component.html',
    styleUrls: ['./transactions-overview.component.scss'],
})
export class TransactionsOverviewComponent {
    isBrowser: boolean;

    constructor(@Inject(PLATFORM_ID) private platformId: Object) {
        this.isBrowser = isPlatformBrowser(platformId);

        // ✅ Enregistre Chart.js uniquement dans le navigateur
        if (this.isBrowser) {
            Chart.register(
                LineController,
                LineElement,
                DoughnutController, // <-- Ajouté
                ArcElement, // <-- Ajouté
                PointElement,
                LinearScale,
                Title,
                CategoryScale,
                Legend,
                Tooltip
            );
        }
    }

    lineChartData: ChartConfiguration<'line'>['data'] = {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May'],
        datasets: [
            {
                data: [1200, 1500, 1000, 2000, 1800],
                label: 'Transactions',
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
            }
        }
    };

    accountChartData = {
        labels: ['Savings', 'Checking', 'Credit'],
        datasets: [{
            data: [45, 30, 25],
            backgroundColor: ['#36a2eb', '#4bc0c0', '#ff6384'],
            borderWidth: 0,
            hoverOffset: 8
        }]
    };

    accountChartOptions: ChartConfiguration<'doughnut'>['options'] = {
        responsive: true,
        maintainAspectRatio: false,
        cutout: '70%',
        plugins: {
            legend: {
                position: 'bottom',
                labels: {
                    boxWidth: 12,
                    padding: 20
                }
            },
            tooltip: {
                enabled: true
            }
        }
    };

}
