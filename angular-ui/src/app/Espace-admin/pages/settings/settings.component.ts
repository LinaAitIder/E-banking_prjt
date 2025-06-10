import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-settings',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule
    ],
    templateUrl: './settings.component.html',
    styleUrls: ['./settings.component.scss']
})
export class SettingsComponent {

    // Taux de change
    exchangeRates = [
        { currency: 'USD', rate: 1 },
        { currency: 'EUR', rate: 0.92 },
        { currency: 'BTC', rate: 0.000023 },
        { currency: 'ETH', rate: 0.00031 }
    ];

    // Limites de transaction
    transactionLimits = {
        dailyLimit: 10000,
        perTransactionLimit: 2500
    };

    // Frais de cryptomonnaie
    cryptoFees = {
        btc: 0.0005,
        eth: 0.001
    };

    

    // Méthodes de sauvegarde simulées
    updateRates() {
        console.log('Exchange Rates:', this.exchangeRates);
        alert('Exchange rates updated!');
    }

    updateLimits() {
        console.log('Transaction Limits:', this.transactionLimits);
        alert('Transaction limits updated!');
    }

    saveCryptoFees() {
        console.log('Crypto Fees:', this.cryptoFees);
        alert('Crypto fees saved!');
    }

    
}

