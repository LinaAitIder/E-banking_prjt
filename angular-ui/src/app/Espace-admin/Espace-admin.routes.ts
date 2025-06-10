import { Routes } from '@angular/router';

import {DashboardComponent} from "./pages/dashboard/dashboard.component";

import {ClientComponent} from "./pages/clients/clients.component";

import {AccountsComponent} from "./pages/accounts/accounts.component";
import {TransactionsComponent} from "./pages/transactions/transactions.component";
import {SettingsComponent} from "./pages/settings/settings.component";
import {SecurityComponent} from "./pages/security/security.component";

export const ADMIN_ROUTES: Routes = [
    { path: 'dashboard', component: DashboardComponent },
    { path: 'clients', component: ClientComponent },
    { path: 'accounts', component: AccountsComponent},
    {path:'transactions',component:TransactionsComponent},
    {path:'settings',component:SettingsComponent},
    {path:'security',component:SecurityComponent},
    { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
];