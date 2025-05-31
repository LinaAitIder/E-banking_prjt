import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-create-client',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './create-client.component.html',
    styleUrls: ['./create-client.component.scss']
})
export class CreateClientComponent {
    client = {
        name: '',
        email: '',
        national_id:'',
        phone: '',
        date_birth:'',
        city:'',
        country:'',
        adress:''
    };

    enregistrer() {
        console.log('Client enrôlé:', this.client);
        alert('Client enrôlé avec succès !');
        this.client = { name: '',  email: '',national_id:'', phone: '',date_birth:'',city:'',country:'',adress:'' };
    }
}
