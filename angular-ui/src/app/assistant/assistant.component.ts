import { Component } from '@angular/core';
import { AiService } from '../services/ai.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TransactionService } from '../services/transaction.service';
import { AccountService } from '../services/account.service';
import { AuthenService } from '../services/authen.service';
import { Client } from '../model/client.model';

interface ChatMessage {
  sender: 'user' | 'ai';
  content: string;
  timestamp: Date;
  loading?: boolean;
}

@Component({
  selector: 'app-assistant',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './assistant.component.html',
  styleUrls: ['./assistant.component.css']
})
export class AssistantComponent {
  userInput = '';
  messages: ChatMessage[] = [];
  sessionId = this.generateSessionId();
  clientId?: number;
  accountId?: number;

  predefinedQuestions = [
    "Quel est mon solde actuel?",
    "Quels sont mes derniers virements?"
  ];

  constructor(
    private aiService: AiService,
    private transactionService: TransactionService,
    private accountService: AccountService,
    private authService: AuthenService
  ) {
    this.loadCurrentClient();
  }

  private loadCurrentClient() {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && 'id' in currentUser) {
      this.clientId = (currentUser as Client).id;
      this.loadClientAccount();
    } else {
      console.error('Aucun client connecté ou utilisateur non reconnu');
      // Optionnel : rediriger vers la page de connexion
    }
  }

  private loadClientAccount() {
    if (!this.clientId) return;

    this.accountService.getAccountsByClientAndType(this.clientId, undefined).subscribe({
      next: (accounts: any[]) => {
        if (accounts.length > 0) {
          this.accountId = accounts[0].id;
        }
      },
      error: (err: any) => {
        console.error('Error loading client account:', err);
      }
    });
  }

  private generateSessionId(): string {
    return 'session-' + Math.random().toString(36).substring(2, 9);
  }

  selectQuestion(question: string): void {
    this.userInput = question;
    this.sendMessage();
  }

  sendMessage(): void {
    if (!this.userInput.trim()) return;
  
    this.messages = [
      ...this.messages,
      { 
        sender: 'user', 
        content: this.userInput,
        timestamp: new Date() 
      },
      {
        sender: 'ai',
        content: '...',
        timestamp: new Date(),
        loading: true
      }
    ];
  
    const messageIndex = this.messages.length - 1;
  
    if (this.userInput.toLowerCase().includes('solde') || 
        this.userInput.toLowerCase().includes('balance')) {
      this.handleBalanceQuery(messageIndex);
    } else if (this.userInput.toLowerCase().includes('virement') || 
               this.userInput.toLowerCase().includes('transaction')) {
      this.handleTransactionQuery(messageIndex);
    } else {
      this.handleGeneralQuery(messageIndex);
    }
  
    this.userInput = '';
  }

  private handleBalanceQuery(messageIndex: number): void {
    if (!this.accountId) {
      this.messages = this.messages.map((msg, index) =>
        index === messageIndex
          ? {
              sender: 'ai',
              content: 'Je ne trouve pas de compte associé à votre profil.',
              timestamp: new Date()
            }
          : msg
      );
      return;
    }

    this.accountService.getAccountDetails(this.accountId).subscribe({
      next: (account: any) => {
        this.messages = this.messages.map((msg, index) => 
          index === messageIndex 
            ? { 
                sender: 'ai',
                content: `Votre solde actuel est de ${account.balance} ${account.currency}.`,
                timestamp: new Date() 
              }
            : msg
        );
      },
      error: (err: any) => {
        console.error('Error fetching account details:', err);
        this.messages = this.messages.map((msg, index) =>
          index === messageIndex
            ? {
                sender: 'ai',
                content: 'Je ne peux pas accéder à votre solde pour le moment.',
                timestamp: new Date()
              }
            : msg
        );
      }
    });
  }

  private handleTransactionQuery(messageIndex: number): void {
    if (!this.accountId) {
      this.messages = this.messages.map((msg, index) =>
        index === messageIndex
          ? {
              sender: 'ai',
              content: 'Je ne trouve pas de compte associé à votre profil.',
              timestamp: new Date()
            }
          : msg
      );
      return;
    }

    this.transactionService.getTransactionsByAccount(this.accountId).subscribe({
      next: (transactions: any[]) => {
        if (transactions.length === 0) {
          this.messages = this.messages.map((msg, index) => 
            index === messageIndex
              ? {
                  sender: 'ai',
                  content: 'Vous n\'avez effectué aucun virement récemment.',
                  timestamp: new Date()
                }
              : msg
          );
          return;
        }

        const transfers = transactions.filter((t: any) => 
          t.type && t.type.toLowerCase().includes('transfer'));
        
        if (transfers.length === 0) {
          this.messages = this.messages.map((msg, index) => 
            index === messageIndex
              ? {
                  sender: 'ai',
                  content: 'Vous n\'avez effectué aucun virement récemment.',
                  timestamp: new Date()
                }
              : msg
          );
          return;
        }

        const recentTransfers = transfers.slice(0, 5);
        
        let response = 'Voici vos derniers virements:\n';
        recentTransfers.forEach((t: any, i: number) => {
          response += `${i+1}. ${t.amount} ${t.currency || '€'} à ${t.destinationUser || t.recipientName || 'destinataire inconnu'} (${t.destinationAccount || t.recipientAccount}) le ${t.transactionDate}\n`;
        });

        this.messages = this.messages.map((msg, index) => 
          index === messageIndex
            ? {
                sender: 'ai',
                content: response,
                timestamp: new Date()
              }
            : msg
        );
      },
      error: (err: any) => {
        console.error('Error fetching transactions:', err);
        this.messages = this.messages.map((msg, index) =>
          index === messageIndex
            ? {
                sender: 'ai',
                content: 'Je ne peux pas accéder à vos transactions pour le moment.',
                timestamp: new Date()
              }
            : msg
        );
      }
    });
  }

  private handleGeneralQuery(messageIndex: number): void {
    this.aiService.askAI(this.userInput).subscribe({
      next: (response: any) => {
        this.messages = this.messages.map((msg, index) => 
          index === messageIndex 
            ? { 
                sender: 'ai',
                content: response.response || "Je n'ai pas pu traiter votre demande.",
                timestamp: new Date() 
              }
            : msg
        );
      },
      error: (err: any) => {
        console.error('Error with AI service:', err);
        this.messages = this.messages.map((msg, index) =>
          index === messageIndex
            ? {
                sender: 'ai',
                content: 'Une erreur est survenue lors de la connexion au service.',
                timestamp: new Date()
              }
            : msg
        );
      }
    });
  }
}