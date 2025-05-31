import { Routes } from '@angular/router';



import {CreateClientComponent} from "./pages/create-client/create-client.component";
import {ManageAbonnesComponent} from "./pages/manage-abonnes/manage-abonnes.component";
import {ProfileComponent} from "./pages/Profile/Profile.component";

export const AGENT_ROUTES: Routes = [
    { path: 'profile', component: ProfileComponent },
    { path: 'clients', component: CreateClientComponent },
    { path: 'abonnes', component: ManageAbonnesComponent},
    { path: '', redirectTo: 'clients', pathMatch: 'full' }
];