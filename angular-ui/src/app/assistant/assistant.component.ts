import { Component } from '@angular/core';
import { AiService } from '../services/ai.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

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

  constructor(private aiService: AiService) {}

  private generateSessionId(): string {
    return 'session-' + Math.random().toString(36).substring(2, 9);
  }

  sendMessage(): void {
    if (!this.userInput.trim()) return;
  
    // Nouveau tableau immuable
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
  
    this.aiService.askAI(this.userInput).subscribe({
      next: (response) => {
        this.messages = this.messages.map((msg, index) => 
          index === messageIndex 
            ? { 
                sender: 'ai',
                content: response.response || "Pas de réponse",
                timestamp: new Date() 
              }
            : msg
        );
      },
      error: (err) => {
        this.messages = this.messages.map((msg, index) =>
          index === messageIndex
            ? {
                sender: 'ai',
                content: 'Erreur de service',
                timestamp: new Date()
              }
            : msg
        );
      }
    });
  
    this.userInput = '';
  }
}