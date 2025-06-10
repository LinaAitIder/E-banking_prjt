import { Component, OnInit } from '@angular/core';
import { AccountRequestService } from '../../../services/account-request.service';
import { AccountRequest } from '../../../model/account-request.model';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-account-requests',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './account-requests.component.html',
    styleUrls: ['./account-requests.component.scss']
})
export class AccountRequestsComponent implements OnInit {
    pendingRequests: AccountRequest[] = [];
    isLoading = false;

    constructor(private accountRequestService: AccountRequestService) {}

    ngOnInit(): void {
        this.loadPendingRequests();
    }

    loadPendingRequests(): void {
        this.isLoading = true;
        this.accountRequestService.getPendingRequests().subscribe({
            next: (requests) => {
                this.pendingRequests = requests;
                this.isLoading = false;
            },
            error: (err) => {
                console.error('Failed to load requests:', err);
                this.isLoading = false;
            }
        });
    }

    approveRequest(requestId: number): void {
        if (!requestId) return;

        this.accountRequestService.approveRequest(requestId).subscribe({
            next: () => {
                this.loadPendingRequests();
            },
            error: (err) => {
                console.error('Approval failed:', err);
            }
        });
    }

    rejectRequest(requestId: number): void {
        if (!requestId) return;

        this.accountRequestService.rejectRequest(requestId).subscribe({
            next: () => {
                this.loadPendingRequests();
            },
            error: (err) => {
                console.error('Rejection failed:', err);
            }
        });
    }
}