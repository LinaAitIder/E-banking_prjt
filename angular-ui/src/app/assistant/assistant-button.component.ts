import { Component, EventEmitter, Output } from '@angular/core';

@Component({
    selector: 'app-assistant-button',
    template: `
      <div class="floating-assistant-container">
        <div class="assistant-label" [class.hidden]="isChatOpen">
          <span class="ai-badge">AI</span> Banko Assistant
        </div>
        <button class="floating-assistant-button" (click)="toggleChat()">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
            <path d="M13 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6z"></path>
            <path d="M9 14a3 3 0 1 0 0-6 3 3 0 0 0 0 6z"></path>
          </svg>
        </button>
      </div>
    `,
    styles: [`
      .floating-assistant-container {
        position: fixed;
        bottom: 30px;
        right: 30px;
        display: flex;
        flex-direction: column;
        align-items: flex-end;
        gap: 10px;
        z-index: 1000;
      }
      
      .assistant-label {
        background: #ff9bb3;
        color: white;
        padding: 10px 15px;
        border-radius: 25px;
        font-size: 14px;
        font-weight: 500;
        box-shadow: 0 2px 12px rgba(255, 155, 179, 0.4);
        opacity: 1;
        transform: translateY(0);
        display: flex;
        align-items: center;
        gap: 8px;
        transition: all 0.3s ease;
      }
      
      .assistant-label.hidden {
        opacity: 0;
        transform: translateY(10px);
        pointer-events: none;
      }
      
      .ai-badge {
        background: white;
        color: #ff9bb3;
        padding: 2px 8px;
        border-radius: 12px;
        font-size: 12px;
        font-weight: bold;
      }
      
      .floating-assistant-button {
        width: 70px;
        height: 70px;
        border-radius: 50%;
        background: linear-gradient(135deg, #ff9bb3, #ff6b8b);
        color: white;
        border: none;
        box-shadow: 0 4px 20px rgba(255, 107, 139, 0.4);
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.3s ease;
      }
      
      .floating-assistant-button:hover {
        background: linear-gradient(135deg, #ff6b8b, #ff4777);
        transform: scale(1.1) rotate(5deg);
      }
      
      .floating-assistant-button svg {
        width: 30px;
        height: 30px;
      }
    `]
})
export class AssistantButtonComponent {
    @Output() chatToggled = new EventEmitter<void>();
    isChatOpen = false;

    toggleChat() {
        this.isChatOpen = !this.isChatOpen;
        this.chatToggled.emit();
    }
}