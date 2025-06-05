import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-manage-abonnes',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './manage-abonnes.component.html',
    styleUrls: ['./manage-abonnes.component.scss']
})
export class ManageAbonnesComponent {
    abonnes = [
        { nom: 'Dupont', email: 'dupont@mail.com' },
        { nom: 'Durand', email: 'durand@mail.com' }
    ];

    supprimer(index: number) {
        this.abonnes.splice(index, 1);
    }
    modifier(index: number) {
        this.abonnes.splice(index, 1);
    }
}
