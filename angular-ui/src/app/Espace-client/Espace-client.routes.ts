import { Routes } from '@angular/router';

import {DashboardComponent} from "./pages/dashboard/dashboard.component";
import {ProfileComponent} from "./pages/profile/profile.component";
import {ClientAccountsComponent} from "./pages/accounts/client-accounts.component";
import {TransactionsComponent} from "../Espace-admin/pages/transactions/transactions.component";
import {ClientTransactionsComponent} from "./pages/transactions/client-transactions.component";

export const CLIENT_ROUTES: Routes = [
    { path: 'dashboard', component: DashboardComponent },
    { path: 'profile', component: ProfileComponent },
    { path: 'accounts', component: ClientAccountsComponent },
    { path: 'transactions', component: ClientTransactionsComponent },
    { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
];