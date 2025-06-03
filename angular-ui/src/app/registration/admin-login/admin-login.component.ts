import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {AuthenService} from "../../services/authen.service";
import { NavbarComponent } from '../../home/navbar/navbar.component';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Router, RouterModule, RouterOutlet } from '@angular/router';

@Component({
    selector: 'app-admin-login',
    standalone: true,
    imports: [ReactiveFormsModule, NavbarComponent, CommonModule, RouterOutlet],
    templateUrl: './admin-login.component.html',
    styleUrls: ['./admin-login.component.scss']
})
export class AdminLoginComponent {
    loginForm: FormGroup;
    passwordChangeForm: FormGroup;
    showPasswordChange = false;
    tempToken = '';

    constructor(
        private fb: FormBuilder,
        private authService: AuthenService,
        private router: Router
    ) {
        this.loginForm = this.fb.group({
            email: ['', [Validators.required, Validators.email]],
            password: ['', Validators.required]
        });

        this.passwordChangeForm = this.fb.group({
            newPassword: ['', [Validators.required, Validators.minLength(8)]]
        });
    }

    onSubmit() {
        if (this.loginForm.valid) {
            this.authService.adminLogin(this.loginForm.value).subscribe({
                next: (response: any) => {
                    // Cas où le changement de mot de passe est requis
                    if (response?.requiresPasswordChange === true && response?.temporaryToken) {
                        this.tempToken = response.temporaryToken;
                        this.showPasswordChange = true;
                    }
                    // Cas où l'authentification réussit
                    else if (response?.token) {
                        localStorage.setItem('token', response.token);
                        this.router.navigate([response.redirect || '/admin/dashboard']);
                    }
                    // Cas où la réponse est invalide
                    else {
                        console.error('Réponse inattendue du serveur:', response);
                        // Afficher un message d'erreur à l'utilisateur
                        alert('Réponse inattendue du serveur. Veuillez contacter l\'administrateur.');
                    }
                },
                error: (err: HttpErrorResponse) => {
                    console.error('Erreur de connexion:', err);
                    // Gestion des erreurs spécifiques
                    if (err.status === 401) {
                        alert('Email ou mot de passe incorrect');
                    } else {
                        alert('Erreur de connexion. Veuillez réessayer plus tard.');
                    }
                }
            });
        } else {
            // Marquer tous les champs comme touchés pour afficher les erreurs de validation
            this.loginForm.markAllAsTouched();
        }
    }

    changePassword() {
        if (this.passwordChangeForm.valid) {
            this.authService.firstLoginChangePassword(
                this.tempToken,
                this.passwordChangeForm.value.newPassword
            ).subscribe({
                next: (response: any) => {
                    if (response?.token) {
                        localStorage.setItem('token', response.token);
                        this.router.navigate(['/admin/dashboard']);
                    } else {
                        console.error('Token missing in response');
                    }
                },
                error: (err: HttpErrorResponse) => {
                    console.error('Password change error:', err);
                }
            });
        }
    }
}
