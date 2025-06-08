import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AgentService } from '../../../services/agent.service';
import { Client } from '../../../model/client.model';


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

    constructor(
      private agentService: AgentService,
      private router: Router
    ) {}

    ngOnInit(): void {
        this.loadAgentClients();
        this.loadUnenrolledClients();
    }

    loadAgentClients(): void {
        this.agentService.getAgentClients().subscribe(clients => {
            this.clients = clients;
        });
    }

    viewClientDetails(clientId: number): void {
      this.router.navigate(['/agent/clients', clientId]);
    }

    loadUnenrolledClients(): void {
        this.agentService.getUnenrolledClients().subscribe(clients => {
            this.unenrolledClients = clients;
        });
    }

    enrollClient(clientId: number): void {
        this.agentService.enrollClient(clientId).subscribe(() => {
            this.loadAgentClients();
            this.loadUnenrolledClients();
        });
    }
}