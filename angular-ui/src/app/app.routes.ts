import { Routes } from '@angular/router';
import { AssistantComponent } from './assistant/assistant.component';
import { DashboardComponent } from './dashboard/dashboard.component';

export const routes: Routes = [
    { path: 'assistant', component: AssistantComponent },
    { path: 'dashboard', component: DashboardComponent },
];
