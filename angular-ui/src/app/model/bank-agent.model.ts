import {User} from "./user.model";


export interface BankAgent extends User {
    role: 'agent';
}