export interface User {
    id: number;
    fullName: string;
    email: string;
    phone: string;
    role : 'client'|'admin'|'agent',
}
