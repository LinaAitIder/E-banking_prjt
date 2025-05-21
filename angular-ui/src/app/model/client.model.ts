import {User} from "./user.model";

export interface Client extends User {
    dateOfBirth: string;
    nationalId: string;
    password: string;
    confirmPassword: string;
    address: string;
    city: string;
    country: string;
    termsAccepted: boolean;
    role:'client'
}
