export interface TransactionResponse {
    id: number;
    transactionDate: string; 
    type: string; 
    amount: number;
  currency?: string;
  destinationUser?: string;
  destinationAccount?: string;
  recipientName?: string;
  recipientAccount?: string;
  sourceUser?: string;
  sourceAccount?: string;

  }


  