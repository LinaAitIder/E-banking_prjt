// src/app/components/transactions/transactions.component.ts
import { Component, OnInit } from '@angular/core';
import { TransactionService } from '../../../services/transaction.service';
import { TransactionResponse } from '../../../model/transaction-response.model';
import { CommonModule } from '@angular/common'; 

@Component({
  selector: 'app-transactions',
  imports: [CommonModule],
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.scss']
})
export class TransactionsComponent implements OnInit {
  transactions: TransactionResponse[] = [];
  loading = true;

  constructor(private transactionService: TransactionService) {}

  ngOnInit(): void {
    this.loadAllTransactions(); 
  }

  loadAllTransactions(): void {
    this.transactionService.getAllTransactions().subscribe({
      next: (data) => {
        this.transactions = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading transactions:', error);
        this.loading = false;
      }
    });
  }

  loadTransactionsByAccount(accountId: number): void {
    this.transactionService.getTransactionsByAccount(accountId).subscribe({
      next: (data) => {
        this.transactions = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading transactions:', error);
        this.loading = false;
      }
    });
  }
}