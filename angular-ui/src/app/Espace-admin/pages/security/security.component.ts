import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-security',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule
    ],
    templateUrl: './security.component.html',
    styleUrls: ['./security.component.scss']
})
export class SecurityComponent {

    twoFactorEnabled = true;
    loginAlerts = false;
    trustedDevices = [
        { name: 'Chrome - Windows', lastUsed: '2025-05-15' },
        { name: 'Safari - iPhone', lastUsed: '2025-05-12' },
    ];

    changePassword() {
        alert('Password changed!');
    }

    toggle2FA() {
        alert('Two-factor authentication updated.');
    }

    saveSettings() {
        alert('Security settings saved!');
    }

    removeDevice(index: number) {
        this.trustedDevices.splice(index, 1);
    }
}
