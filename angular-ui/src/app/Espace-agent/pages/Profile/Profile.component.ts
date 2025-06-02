import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-agent-profile',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './Profile.component.html',
    styleUrls: ['./Profile.component.scss']
})
export class ProfileComponent implements OnInit {
    isEditing = false;

    agent = {
        fullName:'',
        email: '',
        phone: '',
        role: '',
        agency:'',
        agentCode:'',
        motDePasse: '********'
    };

    ngOnInit(): void {
        const storedUserData = localStorage.getItem('userData');
        if (storedUserData) {
            const user = JSON.parse(storedUserData);

            this.agent = {
                fullName:user.fullName || '',
                email: user.email || '',
                phone: user.phone || '',
                role: user.role || '',
                agentCode : user.agentCode ||'',
                agency : user.agency || '',
                motDePasse: '********'
            };
        }
    }

    toggleEdit() {
        this.isEditing = !this.isEditing;
        if (!this.isEditing) {
            console.log('Infos agent mises à jour :', this.agent);
        }
    }
}
