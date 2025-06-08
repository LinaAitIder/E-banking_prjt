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
    role:'client';
    fullName: string;
    email: string;
    phone: string;
    createdAt: Date | string;
    isActive: boolean;
    type?: string; // Individual/Business
    registration?: string; // Date d'inscription formatée

}
