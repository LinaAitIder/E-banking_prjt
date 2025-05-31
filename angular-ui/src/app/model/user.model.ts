export interface User {
    id: number;
    fullName: string;
    email: string;
    phoneNumber: string;
    role : 'client'|'admin'|'agent',
}
