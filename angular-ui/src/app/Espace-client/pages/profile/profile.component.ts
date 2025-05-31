import { Component, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-profile',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss'],
    encapsulation: ViewEncapsulation.None
})
export class ProfileComponent {
    client = {
        name: 'Fatima AITBA',
        role: 'E-BANKING CLIENT',
        email: 'fatima.aitba@example.com',
        phone: '+212 612345678',
        dob: 'March 15,1985',
        address: '123 6123345678',
        rib: '123456789011234567890134',
        nationality: 'Moroccan',
        security: {
            password: '●●●●●●●',
            twoFactor: true
        },
        accounts: [
            { name: 'Checking Account', iban: 'MA64000000117010010106', amount: null },
            { name: 'Savings Account', iban: 'MA6500000000180207067', amount: 5000.00 },
            { name: 'Savings Account', iban: 'MA6600000000180303708', amount: 20000.00 }
        ]
    };

    formatAmount(amount: number | null): string {
        return amount ? '$' + amount.toLocaleString('en-US', { minimumFractionDigits: 2 }) : '';
    }
}