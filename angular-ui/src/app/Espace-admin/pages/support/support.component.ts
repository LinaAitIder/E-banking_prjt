import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-support',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './support.component.html',
    styleUrls: ['./support.component.scss']
})
export class SupportComponent {

    supportTickets = [
        {
            id: 1,
            user: 'john@example.com',
            subject: 'Issue with fund transfer',
            description: 'Funds are stuck during transfer. No confirmation received.',
            status: 'Open',
            priority: 'High',
            date: '2025-05-15'
        },
        {
            id: 2,
            user: 'jane@bankmail.com',
            subject: 'Unable to access account',
            description: 'Password reset not working. Account locked.',
            status: 'Resolved',
            priority: 'Medium',
            date: '2025-05-12'
        },
        {
            id: 3,
            user: 'admin@corp.com',
            subject: 'Incorrect conversion rate displayed',
            description: 'The conversion rate for ETH to USD seems outdated.',
            status: 'Pending',
            priority: 'High',
            date: '2025-05-17'
        },
        {
            id: 4,
            user: 'user@crypto.com',
            subject: 'Crypto wallet not syncing',
            description: 'Wallet not showing latest BTC transactions.',
            status: 'Open',
            priority: 'Low',
            date: '2025-05-16'
        },
    ];

    searchTerm = '';
    filterStatus = 'All';
    filterPriority = 'All';

    currentPage = 1;
    itemsPerPage = 2;

    selectedTicket: any = null;
    showModal = false;

    get filteredTickets() {
        return this.supportTickets.filter(ticket => {
            const matchesSearch = this.searchTerm === '' ||
                ticket.user.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
                ticket.subject.toLowerCase().includes(this.searchTerm.toLowerCase());

            const matchesStatus = this.filterStatus === 'All' || ticket.status === this.filterStatus;
            const matchesPriority = this.filterPriority === 'All' || ticket.priority === this.filterPriority;

            return matchesSearch && matchesStatus && matchesPriority;
        });
    }

    get paginatedTickets() {
        const start = (this.currentPage - 1) * this.itemsPerPage;
        return this.filteredTickets.slice(start, start + this.itemsPerPage);
    }

    totalPages() {
        return Math.ceil(this.filteredTickets.length / this.itemsPerPage);
    }

    resetPagination() {
        this.currentPage = 1;
    }

    nextPage() {
        if (this.currentPage < this.totalPages()) this.currentPage++;
    }

    previousPage() {
        if (this.currentPage > 1) this.currentPage--;
    }

    markAsResolved(ticket: any) {
        ticket.status = 'Resolved';
    }

    openTicketDetails(ticket: any) {
        this.selectedTicket = ticket;
        this.showModal = true;
    }

    closeModal() {
        this.showModal = false;
        this.selectedTicket = null;
    }
}
