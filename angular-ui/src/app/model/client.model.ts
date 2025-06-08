import {User} from "./user.model";

export interface Client extends User {
    id : number;
    dateOfBirth: string;
    nationalId: string;
    password: string;
    confirmPassword: string;
    address: string;
    city: string;
    country: string;
    termsAccepted: boolean;
    challenge : string;
    role:'client'
    isEnrolled: boolean;
    responsibleAgent?: {
            id: number;
            fullName: string;
            agentCode: string;
    } | null;
    accounts?: Account[];
    mainAccount?: Account | null;
}
export interface Account {
    id: number;
    accountNumber: string;
    type: string;
    balance: number;
    currency: string;
    createdAt?: string | Date;
    // Specific to account types
    overdraftLimit?: number;    // CURRENT
    interestRate?: number;      // SAVINGS
    walletAddress?: string;     // CRYPTO

}
