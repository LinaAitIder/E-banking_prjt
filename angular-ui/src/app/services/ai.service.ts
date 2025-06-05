import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AiResponse } from '../model/ai-response.model';


// ai.service.ts
@Injectable({ providedIn: 'root' })
export class AiService {
  private apiUrl = 'http://localhost:8081/E-banking_prjt/api/ai/ask';

  constructor(private http: HttpClient) { }

  askAI(prompt: string): Observable<AiResponse> {
    // Ajoutez des headers et assurez-vous d'envoyer en POST
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    
    return this.http.post<AiResponse>(
      'http://localhost:8081/E-banking_prjt/api/ai/ask', 
      { prompt }, 
      { headers }
    );
  }
}
