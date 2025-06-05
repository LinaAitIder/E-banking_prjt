import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-profile',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss'],
    encapsulation: ViewEncapsulation.None
})
export class ProfileComponent implements OnInit {
    client: any = {};

    ngOnInit(): void {
        const storedUser = localStorage.getItem('userData');
        const userData = storedUser ? JSON.parse(storedUser) : null;

        if (userData) {
            this.client = {
                name: userData.fullName || 'Unknown',
                role: userData.role || 'CLIENT',
                email: userData.email || 'No email',
                phone: userData.phone || 'N/A',
                dob: this.formatDate(userData.dateOfBirth),
                address: userData.address || 'N/A',
                rib: '123456789011234567890134',
                nationality: userData.country || 'N/A',
                security: {
                    password: '●●●●●●●',
                    twoFactor: userData.webAuthnEnabled || false
                },
                accounts: [
                    { name: 'Checking Account', iban: 'MA64000000117010010106', amount: null },
                    { name: 'Savings Account', iban: 'MA6500000000180207067', amount: 5000.00 },
                    { name: 'Savings Account', iban: 'MA6600000000180303708', amount: 20000.00 }
                ]
            };
        }
    }

    formatAmount(amount: number | null): string {
        return amount ? '$' + amount.toLocaleString('en-US', { minimumFractionDigits: 2 }) : '';
    }

    formatDate(timestamp: number): string {
        const date = new Date(timestamp);
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    }
}
