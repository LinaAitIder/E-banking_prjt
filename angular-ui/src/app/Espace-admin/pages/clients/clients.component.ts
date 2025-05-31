import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-client',
    standalone: true,
    imports: [CommonModule,FormsModule],
    templateUrl: './clients.component.html',
    styleUrls: ['./clients.component.scss']
})
export class ClientComponent {

    searchTerm: string = '';  // variable liée à l'input

    clients = [
        { name: 'Michael Williams', id: 'CL101', email: 'williams@ac1', phone: '067 401', type: 'Individual', registration: 'March 2024' },
        { name: 'Susan B.', id: 'CL102', email: 'susan.b@ac2', phone: '123 100', type: 'Business', registration: 'Feb 1, 2024' },
        { name: 'James Johnson', id: 'CL103', email: 'james.johnson', phone: '016 1122', type: 'Individual', registration: 'Mar 23, 2024' },
        { name: 'Patricia Brown', id: 'CL104', email: 'Patricia.brown', phone: '023 456', type: 'Business', registration: 'April 2, 2024' },
        { name: 'Robert Davis', id: 'CL105', email: 'robert@davis', phone: '043 1234', type: 'Individual', registration: 'February 1' },
        { name: 'Davis A.', id: 'CL106', email: 'davis@ac.com', phone: '023 2024', type: 'Individual', registration: 'April 2, 2024' }
    ];
    // Optionnel : filtre clients selon searchTerm
    get filteredClients() {
        const term = this.searchTerm.toLowerCase();

        return this.clients.filter(c =>
            c.name.toLowerCase().includes(term) ||
            c.id.toLowerCase().includes(term) ||
            c.email.toLowerCase().includes(term) ||
            c.phone.toLowerCase().includes(term) ||
            c.type.toLowerCase().includes(term)
        );
    }
}
