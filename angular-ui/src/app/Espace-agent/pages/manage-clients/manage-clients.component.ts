import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AgentService } from '../../../services/agent.service';
import { Client } from '../../../model/client.model';
import { MessageService } from '../../../services/message.service';

@Component({
    selector: 'app-manage-clients',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './manage-clients.component.html',
    styleUrls: ['./manage-clients.component.scss']
})
export class ManageClientsComponent implements OnInit {
    clients: Client[] = [];
    unenrolledClients: Client[] = [];
    isLoading: boolean = true;

    constructor(
      private agentService: AgentService,
      private messageService: MessageService,
      private router: Router
    ) {}

    ngOnInit(): void {
        this.loadAgentClients();
        this.loadUnenrolledClients();
    }

    loadAgentClients(): void {
        this.isLoading = true;
        this.agentService.getAgentClients().subscribe({
            next: (clients: Client[]) => {
                console.log('Received clients:', clients);
                this.clients = clients;
                this.isLoading = false;
            },
            error: (err) => {
                console.error('Erreur complète:', err);
                if (err.status === 500) {
                    alert('Server error. Check backend logs');
                } else {
                    alert('Error while loading clients');
                }
                this.isLoading = false;
            }
        });
    }

    viewClientDetails(clientId: number): void {
      this.router.navigate(['/agent/clients', clientId]);
    }

    refreshClients() {
      this.isLoading = true;
      this.agentService.getAgentClients().subscribe({
        next: (clients) => {
          this.clients = clients;
          this.isLoading = false;
        },
        error: err => {
          console.error('Error loading clients:', err);
          this.isLoading = false;
        }
      });
    }

    loadUnenrolledClients(): void {
        this.isLoading = true;
        this.agentService.getUnenrolledClients().subscribe({
          next: (clients) => {
            this.unenrolledClients = clients;
            this.isLoading = false;
            console.log('Loaded clients:', clients); // Debug
          },
          error: (err) => {
            console.error('Error loading clients:', err);
            this.isLoading = false;
          }
        });
      }

    enrollClient(clientId: number): void {
        this.isLoading = true;
        this.agentService.enrollClient(clientId).subscribe({
          next: () => {
            this.messageService.showSuccess('Enrollment created');
            this.refreshClients();
            this.loadAgentClients();
            this.loadUnenrolledClients();
          },
          error: (err) => {
            console.error('Enrollment failed:', err);
            this.isLoading = false;
          }
        });
    }
}