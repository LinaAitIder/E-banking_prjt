export interface Account {
    id : number;
    accountNumber: string;
    accountType:string;
    holder: string;
    balance: number;
    type: string;
    currency: string;
    overdraftLimit?: number;
    interestRate?: number;
    supportedCryptos?: { [key: string]: string };
    rib: string ;
    createdAt: string;

}



