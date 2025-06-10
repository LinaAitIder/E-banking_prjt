import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransactionsOverviewComponent } from './transactions-overview/transactions-overview.component';
import { TopCardsComponent } from './top-cards/top-cards.component';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [
        CommonModule,
        TransactionsOverviewComponent,
        TopCardsComponent,
        
    ],
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {
    title = 'admin-dashbord';
}
