import { Routes } from '@angular/router';


import { ManageClientsComponent } from "./pages/manage-clients/manage-clients.component";
import { ProfileComponent } from "./pages/Profile/Profile.component";
import { AccountRequestsComponent } from "./pages/account-requests/account-requests.component";
import { ClientDetailsComponent } from "./pages/client-details/client-details.component";

export const AGENT_ROUTES: Routes = [
    { path: 'profile', component: ProfileComponent },
    { path: 'clients', component: ManageClientsComponent },
    { path: 'clients/:id', component: ClientDetailsComponent },
    { path: 'account-requests', component: AccountRequestsComponent },
    { path: '', redirectTo: 'clients', pathMatch: 'full' }
];