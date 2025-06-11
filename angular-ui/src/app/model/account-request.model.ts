export interface AccountRequest {
  id?: number;
  clientId?: number;
  clientName?: string;
  accountType: string; // 'CURRENT' | 'SAVINGS' | 'CRYPTO'
  currency: string;
  overdraftLimit?: number;
  interestRate?: number;
  requestDate?: Date;
  status?: 'PENDING' | 'APPROVED' | 'REJECTED';
}