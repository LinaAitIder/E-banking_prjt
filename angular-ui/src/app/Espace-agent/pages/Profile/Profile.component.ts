import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-agent-profile',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './Profile.component.html',
    styleUrls: ['./Profile.component.scss']
})
export class ProfileComponent {
    isEditing = false;

    agent = {
        prenom: 'Fatima',
        nom: 'Aitba',
        email: 'fatima.aitba@banque.com',
        telephone: '+212600000000',
        matricule: 'AG12345',
        role: 'Conseiller',
        dateEntree: '2021-09-01',
        statut: 'Actif',
        motDePasse: '********'
    };

    toggleEdit() {
        this.isEditing = !this.isEditing;
        if (!this.isEditing) {
            // ici, tu peux envoyer les données modifiées à un service backend
            console.log('Infos agent mises à jour :', this.agent);
        }
    }
}
