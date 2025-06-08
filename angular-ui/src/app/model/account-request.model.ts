export interface AccountRequest {
    accountType: string;
    currency: string;
    overdraftLimit?: number;
    interestRate?: number;
    supportedCryptos?: { [key: string]: string };
}
