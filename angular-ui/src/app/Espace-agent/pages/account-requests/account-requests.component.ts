import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AgentService } from '../../../services/agent.service';
import { AccountRequestService } from '../../../services/account-request.service';
import { MessageService } from '../../../services/message.service';

@Component({
    selector: 'app-account-requests',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './account-requests.component.html',
    styleUrls: ['./account-requests.component.scss']
})
export class AccountRequestsComponent implements OnInit {
    pendingRequests: any[] = [];

    constructor(
        private accountRequestService: AccountRequestService,
        private messageService: MessageService
    ) {}

    ngOnInit(): void {
        this.loadPendingRequests();
    }

    loadPendingRequests(): void {
        this.accountRequestService.getPendingRequests().subscribe({
            next: (requests) => {
                this.pendingRequests = requests;
            },
            error: (err) => {
                this.messageService.showError('Failed to load requests');
                console.error(err);
            }
        });
    }

    approveRequest(requestId: number): void {
        this.accountRequestService.approveRequest(requestId).subscribe({
            next: () => {
                this.messageService.showSuccess('Request approved successfully');
                this.loadPendingRequests();
            },
            error: (err) => {
                this.messageService.showError('Failed to approve request');
                console.error(err);
            }
        });
    }

    rejectRequest(requestId: number): void {
        this.accountRequestService.rejectRequest(requestId).subscribe({
            next: () => {
                this.messageService.showSuccess('Request rejected successfully');
                this.loadPendingRequests();
            },
            error: (err) => {
                this.messageService.showError('Failed to reject request');
                console.error(err);
            }
        });
    }
}