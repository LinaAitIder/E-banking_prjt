import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter, withComponentInputBinding, Routes } from '@angular/router';
import { AppComponent } from './app/app.component';
import {EspaceAdminComponent} from "./app/Espace-admin/Espace-admin.component";
import {EspaceClientComponent} from "./app/Espace-client/Espace-client.component";
import {EspaceAgentComponent} from "./app/Espace-agent/Espace-agent.component"
import {HomeComponent} from "./app/home/home.component";

const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent,
  },
  {
    path: 'admin',
    component: EspaceAdminComponent,
    loadChildren: () => import('./app/Espace-admin/Espace-admin.routes').then(m => m.ADMIN_ROUTES)
  },
  {
    path: 'client',
    component: EspaceClientComponent,
    loadChildren: () => import('./app/Espace-client/Espace-client.routes').then(m => m.CLIENT_ROUTES)
  },

  {
    path: 'agent',
    component: EspaceAgentComponent,
    loadChildren: () => import('./app/Espace-agent/Espace-agent.routes').then(m => m.AGENT_ROUTES)
  },
  { path: '', redirectTo: 'admin', pathMatch: 'full' },
  { path: '**', redirectTo: 'admin' } // Redirection pour les routes inconnues
];

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes, withComponentInputBinding())
  ]
});