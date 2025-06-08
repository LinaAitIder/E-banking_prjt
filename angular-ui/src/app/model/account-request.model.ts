export interface AccountRequest {
  id: number;
  client: {
    id: number;
    fullName: string;
    email: string;
  };
  accountType: 'CURRENT' | 'SAVINGS' | 'CRYPTO';
  currency: string;
  overdraftLimit?: number;       // Pour les comptes CURRENT
  interestRate?: number;         // Pour les comptes SAVINGS
  supportedCryptos?: {           // Pour les comptes CRYPTO
    [key: string]: string;
  };
  requestDate: string;           // Date ISO string
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
}

// Interface pour la creeation de demande
export interface CreateAccountRequest {
  accountType: 'CURRENT' | 'SAVINGS' | 'CRYPTO';
  currency: string;
  overdraftLimit?: number;
  interestRate?: number;
  supportedCryptos?: {
    [key: string]: string;
  };
}

// Interface pour la reponse de l'API
export interface AccountRequestResponse {
  success: boolean;
  message?: string;
  data?: AccountRequest | AccountRequest[];
}

// Type pour les filtres
export type AccountRequestFilter = {
  status?: 'PENDING' | 'APPROVED' | 'REJECTED';
  clientId?: number;
  agentId?: number;
  dateRange?: {
    start: string;
    end: string;
  };
};