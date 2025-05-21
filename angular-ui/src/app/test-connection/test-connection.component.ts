import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TestConnectionService } from '../test-connection.service';

@Component({
  selector: 'app-test-connection',
  standalone: true,
  imports: [CommonModule],
  template: `
    <button (click)="testConnection()">Test Backend Connection</button>
    <div *ngIf="response">
      <p>Response: {{ response }}</p>
    </div>
  `,
  styles: [`
    button {
      padding: 10px 15px;
      background: #1976d2;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
  `]
})
export class TestConnectionComponent {
  response: string | null = null;

  constructor(private testService: TestConnectionService) {}

  testConnection() {
    this.testService.testConnectionS().subscribe({
      next: (res:any) => this.response = res,
      error: (err:any) => this.response = 'Error: ' + err.message
    });
  }
}
