import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

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
    passwordData = {
        currentPassword: '',
        newPassword: '',
        confirmPassword: ''
    };
    errorMessage = '';
    successMessage = '';
    isLoading = false;

    constructor(private http: HttpClient) {}

    changePassword() {
        // Validation frontale
        if (this.passwordData.newPassword !== this.passwordData.confirmPassword) {
            this.errorMessage = 'New passwords do not match';
            return;
        }

        if (this.passwordData.newPassword.length < 8) {
            this.errorMessage = 'Password must be at least 8 characters long';
            return;
        }

        this.isLoading = true;
        this.errorMessage = '';
        this.successMessage = '';

        const token = localStorage.getItem('token'); // Supposons que le token est stocké ici
        if (!token) {
            this.errorMessage = 'Authentication required';
            this.isLoading = false;
            return;
        }

        const request = {
            oldPassword: this.passwordData.currentPassword,
            newPassword: this.passwordData.newPassword
        };

        this.http.post('/api/auth/admin/change-password', request, {
            headers: new HttpHeaders({
                'Authorization': 'Bearer ' + token
            })
        }).pipe(
            catchError(error => {
                this.isLoading = false;
                if (error.status === 400) {
                    this.errorMessage = 'Current password is incorrect';
                } else {
                    this.errorMessage = 'An error occurred. Please try again later.';
                }
                return throwError(error);
            })
        ).subscribe({
            next: () => {
                this.isLoading = false;
                this.successMessage = 'Password changed successfully!';
                this.passwordData = {
                    currentPassword: '',
                    newPassword: '',
                    confirmPassword: ''
                };
                setTimeout(() => this.successMessage = '', 3000);
            },
            error: () => this.isLoading = false
        });
    }

    saveSettings() {
        // Logique de sauvegarde si nécessaire
        alert('Security settings saved!');
    }
}