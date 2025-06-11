import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { AgentService } from '../../../services/agent.service';
import { Client } from '../../../model/client.model';

@Component({
    selector: 'app-client-details',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './client-details.component.html',
    styleUrls: ['./client-details.component.scss']
})
export class ClientDetailsComponent implements OnInit {
    client: Client | null = null;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private agentService: AgentService
    ) {}

    ngOnInit(): void {
        const clientId = this.route.snapshot.paramMap.get('id');
        if (clientId) {
            this.loadClientDetails(parseInt(clientId));
        }
    }

    loadClientDetails(clientId: number): void {
            this.agentService.getClientDetails(clientId).subscribe({
                next: (client) => {
                    // Transformation des donnees si nécessaire
                    this.client = {
                        ...client,
                        isEnrolled: true,
                        accounts: client.accounts?.map(account => ({
                            ...account,
                            createdAt: account.createdAt ? new Date(account.createdAt) : null
                        })) || []
                    };
                },
                error: (err) => {
                    console.error('Failed to load client details', err);
                }
            });
        }

    goBack(): void {
        this.router.navigate(['/agent/clients']);
    }
}