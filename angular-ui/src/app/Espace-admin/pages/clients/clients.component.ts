import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClientService } from '../../../services/client.service';
import { Client } from '../../../model/client.model';

@Component({
    selector: 'app-client',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './clients.component.html',
    styleUrls: ['./clients.component.scss']
})
export class ClientComponent implements OnInit {
    searchTerm: string = '';
    clients: any[] = [];
    loading = true;

    constructor(private clientService: ClientService) {}

    ngOnInit(): void {
        this.loadClients();
    }

    loadClients(): void {
        this.clientService.getAllClients().subscribe({
            next: (clients) => {
                this.clients = clients.map(client => this.mapToDisplayFormat(client));
                this.loading = false;
            },
            error: (err) => {
                console.error('Error loading clients:', err);
                this.loading = false;
            }
        });
    }

    private mapToDisplayFormat(client: Client): any {
        return {
            name: client.fullName,
            id: `CL${client.id.toString().padStart(3, '0')}`,
            email: client.email,
            phone: client.phone,
            registration: this.formatDate(client.createdAt)
        };
    }

    private determineClientType(client: Client): string {
       
        return 'Individual'; 
    }

    private formatDate(date: any): string {
        if (!date) return 'N/A';
        
        try {
            // Gère à la fois les timestamps et les strings ISO
            const dateObj = new Date(date);
            if (isNaN(dateObj.getTime())) return 'N/A';
            
            return dateObj.toLocaleDateString('fr-FR', {
                day: '2-digit',
                month: '2-digit',
                year: 'numeric'
            });
        } catch {
            return 'N/A';
        }
    }
    
    get filteredClients() {
        if (!this.searchTerm) return this.clients; 
        
        const term = this.searchTerm.toLowerCase();
        return this.clients.filter(c =>
            (c.name?.toLowerCase().includes(term) ||
             c.id?.toLowerCase().includes(term) ||
             c.email?.toLowerCase().includes(term) ||
             c.phone?.toLowerCase().includes(term) ||
             c.type?.toLowerCase().includes(term))
        );
    }
}